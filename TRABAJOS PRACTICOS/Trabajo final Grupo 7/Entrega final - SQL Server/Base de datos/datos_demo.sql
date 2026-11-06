-- Clínica Grupo 7 - versión 5.0
USE ClinicaGrupo7;
GO
SET NOCOUNT ON;
SET XACT_ABORT ON;
SET QUOTED_IDENTIFIER ON;
BEGIN TRANSACTION;
-- Datos ficticios de prueba. No se reemplazan registros existentes.
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-001')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-001',N'99000001',N'Lucía Fernández (demo)',N'medico1@example.com',N'Clínica médica',N'Consultorio 1');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-001',1,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-001',3,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-001',5,'08:00','12:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-002')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-002',N'99000002',N'Martín Rodríguez (demo)',N'medico2@example.com',N'Clínica médica',N'Consultorio 2');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-002',2,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-002',4,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-002',6,'14:00','18:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-003')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-003',N'99000003',N'Valentina López (demo)',N'medico3@example.com',N'Pediatría',N'Consultorio 3');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-003',1,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-003',3,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-003',5,'08:00','12:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-004')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-004',N'99000004',N'Santiago Gómez (demo)',N'medico4@example.com',N'Pediatría',N'Consultorio 4');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-004',2,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-004',4,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-004',6,'14:00','18:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-005')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-005',N'99000005',N'Camila Martínez (demo)',N'medico5@example.com',N'Cardiología',N'Consultorio 5');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-005',1,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-005',3,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-005',5,'08:00','12:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-006')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-006',N'99000006',N'Nicolás Pérez (demo)',N'medico6@example.com',N'Cardiología',N'Consultorio 6');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-006',2,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-006',4,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-006',6,'14:00','18:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-007')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-007',N'99000007',N'Florencia Díaz (demo)',N'medico7@example.com',N'Dermatología',N'Consultorio 7');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-007',1,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-007',3,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-007',5,'08:00','12:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-008')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-008',N'99000008',N'Tomás Sánchez (demo)',N'medico8@example.com',N'Dermatología',N'Consultorio 8');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-008',2,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-008',4,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-008',6,'14:00','18:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-009')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-009',N'99000009',N'Julieta Romero (demo)',N'medico9@example.com',N'Traumatología',N'Consultorio 9');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-009',1,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-009',3,'08:00','12:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-009',5,'08:00','12:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Medicos WHERE matricula=N'DEMO-010')
BEGIN
 INSERT INTO dbo.Medicos(matricula,dni,nombre,email,especialidad,consultorio) VALUES(N'DEMO-010',N'99000010',N'Federico Torres (demo)',N'medico10@example.com',N'Traumatología',N'Consultorio 10');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-010',2,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-010',4,'14:00','18:00');
 INSERT INTO dbo.HorariosMedico(matricula,dia,desde,hasta) VALUES(N'DEMO-010',6,'14:00','18:00');
END;
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000001') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000001',N'Ana Pérez (demo 001)',N'paciente1@example.com',N'DEMO-HC-001',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000002') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000002',N'Juan Pérez (demo 002)',N'paciente2@example.com',N'DEMO-HC-002',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000003') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000003',N'Sofía Pérez (demo 003)',N'paciente3@example.com',N'DEMO-HC-003',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000004') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000004',N'Mateo Pérez (demo 004)',N'paciente4@example.com',N'DEMO-HC-004',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000005') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000005',N'Valeria Pérez (demo 005)',N'paciente5@example.com',N'DEMO-HC-005',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000006') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000006',N'Lucas Pérez (demo 006)',N'paciente6@example.com',N'DEMO-HC-006',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000007') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000007',N'María Pérez (demo 007)',N'paciente7@example.com',N'DEMO-HC-007',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000008') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000008',N'Diego Pérez (demo 008)',N'paciente8@example.com',N'DEMO-HC-008',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000009') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000009',N'Carolina Pérez (demo 009)',N'paciente9@example.com',N'DEMO-HC-009',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000010') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000010',N'Bruno Pérez (demo 010)',N'paciente10@example.com',N'DEMO-HC-010',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000011') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000011',N'Paula Pérez (demo 011)',N'paciente11@example.com',N'DEMO-HC-011',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000012') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000012',N'Agustín Pérez (demo 012)',N'paciente12@example.com',N'DEMO-HC-012',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000013') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000013',N'Victoria Pérez (demo 013)',N'paciente13@example.com',N'DEMO-HC-013',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000014') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000014',N'Joaquín Pérez (demo 014)',N'paciente14@example.com',N'DEMO-HC-014',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000015') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000015',N'Daniela Pérez (demo 015)',N'paciente15@example.com',N'DEMO-HC-015',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000016') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000016',N'Manuel Pérez (demo 016)',N'paciente16@example.com',N'DEMO-HC-016',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000017') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000017',N'Emilia Pérez (demo 017)',N'paciente17@example.com',N'DEMO-HC-017',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000018') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000018',N'Facundo Pérez (demo 018)',N'paciente18@example.com',N'DEMO-HC-018',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000019') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000019',N'Natalia Pérez (demo 019)',N'paciente19@example.com',N'DEMO-HC-019',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000020') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000020',N'Gabriel Pérez (demo 020)',N'paciente20@example.com',N'DEMO-HC-020',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000021') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000021',N'Ana García (demo 021)',N'paciente21@example.com',N'DEMO-HC-021',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000022') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000022',N'Juan García (demo 022)',N'paciente22@example.com',N'DEMO-HC-022',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000023') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000023',N'Sofía García (demo 023)',N'paciente23@example.com',N'DEMO-HC-023',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000024') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000024',N'Mateo García (demo 024)',N'paciente24@example.com',N'DEMO-HC-024',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000025') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000025',N'Valeria García (demo 025)',N'paciente25@example.com',N'DEMO-HC-025',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000026') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000026',N'Lucas García (demo 026)',N'paciente26@example.com',N'DEMO-HC-026',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000027') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000027',N'María García (demo 027)',N'paciente27@example.com',N'DEMO-HC-027',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000028') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000028',N'Diego García (demo 028)',N'paciente28@example.com',N'DEMO-HC-028',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000029') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000029',N'Carolina García (demo 029)',N'paciente29@example.com',N'DEMO-HC-029',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000030') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000030',N'Bruno García (demo 030)',N'paciente30@example.com',N'DEMO-HC-030',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000031') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000031',N'Paula García (demo 031)',N'paciente31@example.com',N'DEMO-HC-031',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000032') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000032',N'Agustín García (demo 032)',N'paciente32@example.com',N'DEMO-HC-032',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000033') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000033',N'Victoria García (demo 033)',N'paciente33@example.com',N'DEMO-HC-033',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000034') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000034',N'Joaquín García (demo 034)',N'paciente34@example.com',N'DEMO-HC-034',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000035') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000035',N'Daniela García (demo 035)',N'paciente35@example.com',N'DEMO-HC-035',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000036') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000036',N'Manuel García (demo 036)',N'paciente36@example.com',N'DEMO-HC-036',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000037') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000037',N'Emilia García (demo 037)',N'paciente37@example.com',N'DEMO-HC-037',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000038') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000038',N'Facundo García (demo 038)',N'paciente38@example.com',N'DEMO-HC-038',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000039') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000039',N'Natalia García (demo 039)',N'paciente39@example.com',N'DEMO-HC-039',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000040') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000040',N'Gabriel García (demo 040)',N'paciente40@example.com',N'DEMO-HC-040',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000041') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000041',N'Ana López (demo 041)',N'paciente41@example.com',N'DEMO-HC-041',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000042') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000042',N'Juan López (demo 042)',N'paciente42@example.com',N'DEMO-HC-042',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000043') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000043',N'Sofía López (demo 043)',N'paciente43@example.com',N'DEMO-HC-043',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000044') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000044',N'Mateo López (demo 044)',N'paciente44@example.com',N'DEMO-HC-044',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000045') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000045',N'Valeria López (demo 045)',N'paciente45@example.com',N'DEMO-HC-045',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000046') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000046',N'Lucas López (demo 046)',N'paciente46@example.com',N'DEMO-HC-046',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000047') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000047',N'María López (demo 047)',N'paciente47@example.com',N'DEMO-HC-047',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000048') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000048',N'Diego López (demo 048)',N'paciente48@example.com',N'DEMO-HC-048',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000049') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000049',N'Carolina López (demo 049)',N'paciente49@example.com',N'DEMO-HC-049',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000050') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000050',N'Bruno López (demo 050)',N'paciente50@example.com',N'DEMO-HC-050',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000051') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000051',N'Paula López (demo 051)',N'paciente51@example.com',N'DEMO-HC-051',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000052') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000052',N'Agustín López (demo 052)',N'paciente52@example.com',N'DEMO-HC-052',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000053') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000053',N'Victoria López (demo 053)',N'paciente53@example.com',N'DEMO-HC-053',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000054') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000054',N'Joaquín López (demo 054)',N'paciente54@example.com',N'DEMO-HC-054',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000055') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000055',N'Daniela López (demo 055)',N'paciente55@example.com',N'DEMO-HC-055',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000056') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000056',N'Manuel López (demo 056)',N'paciente56@example.com',N'DEMO-HC-056',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000057') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000057',N'Emilia López (demo 057)',N'paciente57@example.com',N'DEMO-HC-057',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000058') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000058',N'Facundo López (demo 058)',N'paciente58@example.com',N'DEMO-HC-058',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000059') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000059',N'Natalia López (demo 059)',N'paciente59@example.com',N'DEMO-HC-059',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000060') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000060',N'Gabriel López (demo 060)',N'paciente60@example.com',N'DEMO-HC-060',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000061') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000061',N'Ana Gómez (demo 061)',N'paciente61@example.com',N'DEMO-HC-061',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000062') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000062',N'Juan Gómez (demo 062)',N'paciente62@example.com',N'DEMO-HC-062',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000063') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000063',N'Sofía Gómez (demo 063)',N'paciente63@example.com',N'DEMO-HC-063',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000064') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000064',N'Mateo Gómez (demo 064)',N'paciente64@example.com',N'DEMO-HC-064',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000065') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000065',N'Valeria Gómez (demo 065)',N'paciente65@example.com',N'DEMO-HC-065',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000066') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000066',N'Lucas Gómez (demo 066)',N'paciente66@example.com',N'DEMO-HC-066',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000067') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000067',N'María Gómez (demo 067)',N'paciente67@example.com',N'DEMO-HC-067',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000068') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000068',N'Diego Gómez (demo 068)',N'paciente68@example.com',N'DEMO-HC-068',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000069') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000069',N'Carolina Gómez (demo 069)',N'paciente69@example.com',N'DEMO-HC-069',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000070') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000070',N'Bruno Gómez (demo 070)',N'paciente70@example.com',N'DEMO-HC-070',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000071') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000071',N'Paula Gómez (demo 071)',N'paciente71@example.com',N'DEMO-HC-071',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000072') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000072',N'Agustín Gómez (demo 072)',N'paciente72@example.com',N'DEMO-HC-072',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000073') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000073',N'Victoria Gómez (demo 073)',N'paciente73@example.com',N'DEMO-HC-073',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000074') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000074',N'Joaquín Gómez (demo 074)',N'paciente74@example.com',N'DEMO-HC-074',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000075') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000075',N'Daniela Gómez (demo 075)',N'paciente75@example.com',N'DEMO-HC-075',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000076') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000076',N'Manuel Gómez (demo 076)',N'paciente76@example.com',N'DEMO-HC-076',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000077') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000077',N'Emilia Gómez (demo 077)',N'paciente77@example.com',N'DEMO-HC-077',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000078') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000078',N'Facundo Gómez (demo 078)',N'paciente78@example.com',N'DEMO-HC-078',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000079') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000079',N'Natalia Gómez (demo 079)',N'paciente79@example.com',N'DEMO-HC-079',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000080') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000080',N'Gabriel Gómez (demo 080)',N'paciente80@example.com',N'DEMO-HC-080',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000081') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000081',N'Ana Fernández (demo 081)',N'paciente81@example.com',N'DEMO-HC-081',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000082') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000082',N'Juan Fernández (demo 082)',N'paciente82@example.com',N'DEMO-HC-082',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000083') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000083',N'Sofía Fernández (demo 083)',N'paciente83@example.com',N'DEMO-HC-083',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000084') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000084',N'Mateo Fernández (demo 084)',N'paciente84@example.com',N'DEMO-HC-084',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000085') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000085',N'Valeria Fernández (demo 085)',N'paciente85@example.com',N'DEMO-HC-085',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000086') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000086',N'Lucas Fernández (demo 086)',N'paciente86@example.com',N'DEMO-HC-086',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000087') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000087',N'María Fernández (demo 087)',N'paciente87@example.com',N'DEMO-HC-087',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000088') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000088',N'Diego Fernández (demo 088)',N'paciente88@example.com',N'DEMO-HC-088',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000089') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000089',N'Carolina Fernández (demo 089)',N'paciente89@example.com',N'DEMO-HC-089',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000090') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000090',N'Bruno Fernández (demo 090)',N'paciente90@example.com',N'DEMO-HC-090',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000091') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000091',N'Paula Fernández (demo 091)',N'paciente91@example.com',N'DEMO-HC-091',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000092') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000092',N'Agustín Fernández (demo 092)',N'paciente92@example.com',N'DEMO-HC-092',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000093') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000093',N'Victoria Fernández (demo 093)',N'paciente93@example.com',N'DEMO-HC-093',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000094') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000094',N'Joaquín Fernández (demo 094)',N'paciente94@example.com',N'DEMO-HC-094',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000095') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000095',N'Daniela Fernández (demo 095)',N'paciente95@example.com',N'DEMO-HC-095',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000096') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000096',N'Manuel Fernández (demo 096)',N'paciente96@example.com',N'DEMO-HC-096',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000097') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000097',N'Emilia Fernández (demo 097)',N'paciente97@example.com',N'DEMO-HC-097',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000098') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000098',N'Facundo Fernández (demo 098)',N'paciente98@example.com',N'DEMO-HC-098',N'DEMO-002');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000099') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000099',N'Natalia Fernández (demo 099)',N'paciente99@example.com',N'DEMO-HC-099',N'DEMO-001');
IF NOT EXISTS(SELECT 1 FROM dbo.Pacientes WHERE dni=N'98000100') INSERT INTO dbo.Pacientes(dni,nombre,email,historia,cabecera) VALUES(N'98000100',N'Gabriel Fernández (demo 100)',N'paciente100@example.com',N'DEMO-HC-100',N'DEMO-002');
COMMIT;
