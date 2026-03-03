USE landside;

DELIMITER $$

DROP PROCEDURE IF EXISTS benchmark_sort $$
CREATE PROCEDURE benchmark_sort()
BEGIN
    DECLARE i INT DEFAULT 1;
    DECLARE t0, t1 DATETIME(6);
    DECLARE elapsed DECIMAL(10,6);
    DECLARE total_no_idx DECIMAL(10,6) DEFAULT 0;
    DECLARE total_idx DECIMAL(10,6) DEFAULT 0;

    -- ======================
    -- No Index case
    -- ======================
    WHILE i <= 10 DO
            SET t0 = NOW(6);
            DROP TEMPORARY TABLE IF EXISTS _sorted_sink;
            CREATE TEMPORARY TABLE _sorted_sink ENGINE=InnoDB AS
            SELECT appointment_id
            FROM appointments
            ORDER BY arrival_window_start;
            SET t1 = NOW(6);

            SET elapsed = TIMESTAMPDIFF(MICROSECOND, t0, t1) / 1000000;
            SET total_no_idx = total_no_idx + elapsed;

            SET i = i + 1;
        END WHILE;

    -- ======================
    -- With Index case
    -- ======================
    -- Uncomment the line below after the initial run
#     DROP INDEX idx_arrival_window_start ON appointments;
    CREATE INDEX idx_arrival_window_start ON appointments (arrival_window_start);
    SET i = 1;

    WHILE i <= 10 DO
            SET t0 = NOW(6);
            DROP TEMPORARY TABLE IF EXISTS _sorted_sink;
            CREATE TEMPORARY TABLE _sorted_sink ENGINE=InnoDB AS
            SELECT appointment_id
            FROM appointments
            ORDER BY arrival_window_start;
            SET t1 = NOW(6);

            SET elapsed = TIMESTAMPDIFF(MICROSECOND, t0, t1) / 1000000;
            SET total_idx = total_idx + elapsed;

            SET i = i + 1;
        END WHILE;

    -- ======================
    -- Output results
    -- ======================
    SELECT 'no_index' AS case_name, ROUND(total_no_idx / 10, 6) AS avg_seconds
    UNION ALL
    SELECT 'with_index', ROUND(total_idx / 10, 6);
END $$
DELIMITER ;

-- Run the benchmark
CALL benchmark_sort();

-- Example Run Result
# no_index,   0.099394
# with_index, 0.091488
# Conclusion: Placing and index on `arrival_window_start` doesn't speed things up significantly
