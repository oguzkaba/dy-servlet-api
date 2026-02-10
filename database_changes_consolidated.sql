-- =============================================================================
-- EBÜLTEN & DİNAMİK RANDEVU ÜCRETİ SİSTEMİ - TÜM VERİTABANI DEĞİŞİKLİKLERİ
-- =============================================================================
-- Bu dosya tüm yeni tabloları ve stored procedure'leri içermektedir.
-- Sunucu terminalinden veya bir DB istemcisinden (MySQL Workbench, Navicat vb.) 
-- toplu olarak çalıştırılabilir.

-- -----------------------------------------------------------------------------
-- 1. ET_APPOINTMENT_PRICE TABLOSU (Doktor Seans Ücretleri)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `et_appointment_price` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `doctor_id` VARCHAR(60) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `isActive` TINYINT(1) DEFAULT '1',
  `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_doctor_id` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 2. PAYMENTS TABLOSU (Ödeme Hareketleri İzleme)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `payments` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `appointment_id` BIGINT NOT NULL,
  `patient_id` VARCHAR(60) NOT NULL,
  `payment_id` VARCHAR(100) NOT NULL,
  `payment_transaction_id` VARCHAR(100) DEFAULT NULL,
  `price` DECIMAL(19,4) NOT NULL,
  `status` VARCHAR(20) NOT NULL,
  `raw_result` LONGTEXT DEFAULT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_id` (`payment_id`),
  KEY `idx_appointment_id` (`appointment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- -----------------------------------------------------------------------------
-- 3. STORED PROCEDURE: addPaymentRecord
-- Iyzico'dan ödeme başarılı uyarısı geldiği anda bu kayıt atılır.
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `addPaymentRecord`;

DELIMITER //
CREATE PROCEDURE `addPaymentRecord`(
    IN p_appointment_id BIGINT,
    IN p_patient_id VARCHAR(60),
    IN p_payment_id VARCHAR(100),
    IN p_payment_transaction_id VARCHAR(100),
    IN p_price DECIMAL(19,4),
    IN p_status VARCHAR(20),
    IN p_raw_result LONGTEXT
)
BEGIN
    INSERT INTO payments (appointment_id, patient_id, payment_id, payment_transaction_id, price, status, raw_result)
    VALUES (p_appointment_id, p_patient_id, p_payment_id, p_payment_transaction_id, p_price, p_status, p_raw_result);
END //
DELIMITER ;

-- -----------------------------------------------------------------------------
-- 4. STORED PROCEDURE: finalizeAppointmentPayment
-- Aktivasyon başarılı olduktan sonra randevuyu kapatır.
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `finalizeAppointmentPayment`;

DELIMITER //
CREATE PROCEDURE `finalizeAppointmentPayment`(
    IN p_appointment_id BIGINT,
    IN p_payment_id VARCHAR(100)
)
BEGIN
    UPDATE et_appointment 
    SET payment_id = p_payment_id, 
        payment_date = NOW()
    WHERE id = p_appointment_id;
END //
DELIMITER ;

-- -----------------------------------------------------------------------------
-- 5. STORED PROCEDURE: getAppointmentFinalPrice
-- Dinamik ücretlendirme (Doktor ücreti + İlk randevu ücretsiz kontrolü).
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS `getAppointmentFinalPrice`;

DELIMITER //
CREATE PROCEDURE `getAppointmentFinalPrice`(
    IN p_doctor_id VARCHAR(60),
    IN p_patient_id VARCHAR(60),
    OUT p_final_price DECIMAL(10,2)
)
BEGIN
    DECLARE v_doctor_fee DECIMAL(10,2) DEFAULT 0.00;
    DECLARE v_is_first_free TINYINT(1) DEFAULT 0;
    DECLARE v_visit_count INT DEFAULT 0;

    -- 1. Doktorun aktif seans ücretini al
    SELECT IFNULL(price, 0.00) INTO v_doctor_fee 
    FROM et_appointment_price 
    WHERE doctor_id = p_doctor_id AND isActive = 1 
    LIMIT 1;

    -- 2. Doktorun "İlk Görüşme Ücretsiz" ayarını kontrol et
    SELECT IFNULL(isFirstVisitFree, 0) INTO v_is_first_free 
    FROM doctor_profile 
    WHERE doctorID = p_doctor_id 
    LIMIT 1;

    -- 3. Hastanın bu doktorla daha önce aktif/tamamlanmış randevusu var mı?
    -- NOT: appointment_status değerleri projenizdeki statülerle (ACTIVE, COMPLETED vb.) tam uyuşmalıdır.
    SELECT COUNT(*) INTO v_visit_count 
    FROM et_appointment 
    WHERE patient_id = p_patient_id 
      AND doctor_id = p_doctor_id 
      AND appointment_status IN ('ACTIVE', 'COMPLETED');

    -- MANTIK: Eğer ilk görüşme ücretsizse ve daha önce görüşme yoksa fiyat 0.00'dır.
    IF v_is_first_free = 1 AND v_visit_count = 0 THEN
        SET p_final_price = 0.00;
    ELSE
        SET p_final_price = v_doctor_fee;
    END IF;
END //
DELIMITER ;

-- =============================================================================
-- SQL SCRIPT SONU
-- =============================================================================
