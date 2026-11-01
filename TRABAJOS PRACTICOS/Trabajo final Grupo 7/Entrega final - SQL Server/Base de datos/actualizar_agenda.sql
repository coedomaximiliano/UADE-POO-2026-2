USE ClinicaGrupo7;
GO
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
