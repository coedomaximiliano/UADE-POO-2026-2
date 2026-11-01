USE ClinicaGrupo7;
GO
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
