-- =====================================================
-- SCRIPT DE RESPALDO: Tabla ventas
-- Fecha: 2025-10-06
-- Descripción: Respaldo de datos antes de recrear tabla
-- =====================================================

-- Crear tabla temporal de respaldo
CREATE TABLE IF NOT EXISTS ventas_backup AS
SELECT * FROM ventas;

-- Verificar cantidad de registros respaldados
SELECT COUNT(*) AS total_registros_respaldados FROM ventas_backup;
