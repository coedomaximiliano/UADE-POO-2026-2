SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET ARITHABORT ON;
SET NUMERIC_ROUNDABORT OFF;
GO
-- Ejecutar con sqlcmd -S .\SQLEXPRESS -E -C -i crear_base.sql
IF DB_ID(N'ClinicaGrupo7') IS NULL
    CREATE DATABASE ClinicaGrupo7;
GO
USE ClinicaGrupo7;
GO
IF OBJECT_ID(N'dbo.Pacientes', N'U') IS NULL
CREATE TABLE dbo.Pacientes (
    dni NVARCHAR(30) NOT NULL PRIMARY KEY,
    nombre NVARCHAR(150) NOT NULL,
    email NVARCHAR(254) NOT NULL DEFAULT '',
    historia NVARCHAR(50) NOT NULL UNIQUE
);
IF OBJECT_ID(N'dbo.Medicos', N'U') IS NULL
CREATE TABLE dbo.Medicos (
    matricula NVARCHAR(50) NOT NULL PRIMARY KEY,
    dni NVARCHAR(30) NOT NULL UNIQUE,
    nombre NVARCHAR(150) NOT NULL,
    email NVARCHAR(254) NOT NULL DEFAULT '',
    especialidad NVARCHAR(100) NOT NULL
);
IF OBJECT_ID(N'dbo.SecuenciaTurnos', N'SO') IS NULL
    CREATE SEQUENCE dbo.SecuenciaTurnos AS INT START WITH 1 INCREMENT BY 1;
IF OBJECT_ID(N'dbo.Turnos', N'U') IS NULL
CREATE TABLE dbo.Turnos (
    id INT NOT NULL PRIMARY KEY,
    paciente_dni NVARCHAR(30) NOT NULL REFERENCES dbo.Pacientes(dni),
    medico_matricula NVARCHAR(50) NOT NULL REFERENCES dbo.Medicos(matricula),
    fecha DATE NOT NULL,
    hora TIME(0) NOT NULL,
    estado VARCHAR(10) NOT NULL DEFAULT 'PENDIENTE',
    CONSTRAINT CK_Turnos_Estado CHECK (estado IN ('PENDIENTE','ATENDIDO','CANCELADO')),
    CONSTRAINT CK_Turnos_Hora CHECK (DATEPART(MINUTE,hora) IN (0,30) AND DATEPART(SECOND,hora)=0)
);
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name='UX_Turno_Medico' AND object_id=OBJECT_ID('dbo.Turnos'))
CREATE UNIQUE INDEX UX_Turno_Medico ON dbo.Turnos(medico_matricula,fecha,hora) WHERE estado <> 'CANCELADO';
IF NOT EXISTS (SELECT 1 FROM sys.indexes WHERE name='UX_Turno_Paciente' AND object_id=OBJECT_ID('dbo.Turnos'))
CREATE UNIQUE INDEX UX_Turno_Paciente ON dbo.Turnos(paciente_dni,fecha,hora) WHERE estado <> 'CANCELADO';
GO

-- Agenda semanal y médico de cabecera
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET XACT_ABORT ON;
GO
IF COL_LENGTH('dbo.Medicos','consultorio') IS NULL
    ALTER TABLE dbo.Medicos ADD consultorio NVARCHAR(60) NOT NULL CONSTRAINT DF_Medicos_Consultorio DEFAULT N'Sin asignar';
IF COL_LENGTH('dbo.Pacientes','cabecera') IS NULL
    ALTER TABLE dbo.Pacientes ADD cabecera NVARCHAR(50) NULL;
GO
IF NOT EXISTS (SELECT 1 FROM sys.foreign_keys WHERE name='FK_Paciente_Cabecera')
    ALTER TABLE dbo.Pacientes ADD CONSTRAINT FK_Paciente_Cabecera FOREIGN KEY(cabecera) REFERENCES dbo.Medicos(matricula);
IF OBJECT_ID('dbo.HorariosMedico','U') IS NULL
CREATE TABLE dbo.HorariosMedico (
    matricula NVARCHAR(50) NOT NULL REFERENCES dbo.Medicos(matricula) ON DELETE CASCADE,
    dia TINYINT NOT NULL CHECK(dia BETWEEN 1 AND 7),
    desde TIME(0) NOT NULL,
    hasta TIME(0) NOT NULL,
    PRIMARY KEY(matricula,dia),
    CHECK(desde<hasta),
    CHECK(DATEPART(MINUTE,desde) IN (0,30) AND DATEPART(SECOND,desde)=0),
    CHECK(DATEPART(MINUTE,hasta) IN (0,30) AND DATEPART(SECOND,hasta)=0)
);
GO
CREATE OR ALTER TRIGGER dbo.TR_Turno_Horario ON dbo.Turnos AFTER INSERT, UPDATE AS
BEGIN
 SET NOCOUNT ON;
 -- Cambiar el estado no debe invalidar los turnos anteriores a esta mejora.
 IF UPDATE(fecha) OR UPDATE(hora) OR UPDATE(medico_matricula)
 BEGIN
  IF EXISTS (
   SELECT 1 FROM inserted i WHERE i.estado<>'CANCELADO' AND NOT EXISTS (
    SELECT 1 FROM dbo.HorariosMedico h WHERE h.matricula=i.medico_matricula
     AND h.dia=(DATEDIFF(DAY,CONVERT(date,'19000101'),i.fecha)%7)+1
     AND i.hora>=h.desde AND i.hora<=DATEADD(MINUTE,-30,h.hasta)
   )
  ) THROW 50001, 'El medico no atiende en ese dia u horario.', 1;
 END
END;
GO

-- Ausencias y vacaciones
SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
SET ANSI_PADDING ON;
SET ANSI_WARNINGS ON;
SET CONCAT_NULL_YIELDS_NULL ON;
SET ARITHABORT ON;
SET NUMERIC_ROUNDABORT OFF;
SET XACT_ABORT ON;
GO
IF OBJECT_ID('dbo.AusenciasMedico','U') IS NULL
CREATE TABLE dbo.AusenciasMedico (
 id INT IDENTITY PRIMARY KEY,
 matricula NVARCHAR(50) NOT NULL REFERENCES dbo.Medicos(matricula),
 desde DATE NOT NULL,
 hasta DATE NOT NULL,
 motivo NVARCHAR(120) NOT NULL,
 activa BIT NOT NULL DEFAULT 1,
 CHECK(desde<=hasta), CHECK(LEN(LTRIM(RTRIM(motivo)))>0)
);
GO
-- El bloqueo por matrícula también se toma al reservar o reasignar.
-- Una ausencia nueva conserva las reservas que ya estaban cargadas.
CREATE OR ALTER TRIGGER dbo.TR_Turno_Ausencia ON dbo.Turnos AFTER INSERT,UPDATE AS
BEGIN
 SET NOCOUNT ON;
 IF UPDATE(fecha) OR UPDATE(hora) OR UPDATE(medico_matricula)
 BEGIN
  DECLARE @n INT;
  SELECT @n=COUNT(*) FROM dbo.Medicos m WITH(UPDLOCK,HOLDLOCK)
   JOIN inserted i ON i.medico_matricula=m.matricula;
  IF EXISTS(SELECT 1 FROM inserted i JOIN dbo.AusenciasMedico a
   ON a.matricula=i.medico_matricula AND a.activa=1
   AND i.fecha BETWEEN a.desde AND a.hasta WHERE i.estado<>'CANCELADO')
    THROW 50002,'El medico tiene una ausencia registrada en esa fecha.',1;
 END
END;
GO
