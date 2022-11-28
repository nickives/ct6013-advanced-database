--------------------------------------------------------
--  File created - Sunday-November-27-2022   
--------------------------------------------------------
ALTER SESSION SET CONTAINER = XEPDB1;
--------------------------------------------------------
--  DDL for Table COURSE
--------------------------------------------------------

  CREATE TABLE "S1909632"."COURSE" 
   (	"ID" NUMBER GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"NAME" NVARCHAR2(20)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Table LECTURER
--------------------------------------------------------

  CREATE TABLE "S1909632"."LECTURER" 
   (	"ID" NUMBER GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"NAME" NVARCHAR2(20)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Table MODULE
--------------------------------------------------------

  CREATE TABLE "S1909632"."MODULE" 
   (	"ID" NUMBER GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"NAME" NVARCHAR2(20), 
	"CODE" VARCHAR2(20 BYTE), 
	"SEMESTER" VARCHAR2(20 BYTE), 
	"CAT_POINTS" NUMBER, 
	"LECTURER_ID" NUMBER, 
	"COURSE_ID" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Table STUDENT
--------------------------------------------------------

  CREATE TABLE "S1909632"."STUDENT" 
   (	"ID" NUMBER GENERATED ALWAYS AS IDENTITY MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE  NOKEEP  NOSCALE , 
	"FIRST_NAME" NVARCHAR2(20), 
	"LAST_NAME" NVARCHAR2(20), 
	"STUDENT_NUMBER" NVARCHAR2(20)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Table STUDENT_COURSE
--------------------------------------------------------

  CREATE TABLE "S1909632"."STUDENT_COURSE" 
   (	"STUDENT_ID" NUMBER, 
	"COURSE_ID" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Table STUDENT_MODULE
--------------------------------------------------------

  CREATE TABLE "S1909632"."STUDENT_MODULE" 
   (	"STUDENT_ID" NUMBER, 
	"MODULE_ID" NUMBER, 
	"MARK" NUMBER
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index COURSE_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."COURSE_NAME" ON "S1909632"."COURSE" ("NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index COURSE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."COURSE_PK" ON "S1909632"."COURSE" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index LECTURERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."LECTURERS_PK" ON "S1909632"."LECTURER" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index MODULE_CODE_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."MODULE_CODE_UK" ON "S1909632"."MODULE" ("CODE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index MODULE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."MODULE_PK" ON "S1909632"."MODULE" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_COURSE_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_COURSE_pk" ON "S1909632"."STUDENT_COURSE" ("COURSE_ID", "STUDENT_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_MODULE_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_MODULE_pk" ON "S1909632"."STUDENT_MODULE" ("STUDENT_ID", "MODULE_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_STUDENT_NUMBER_uindex
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_STUDENT_NUMBER_uindex" ON "S1909632"."STUDENT" ("STUDENT_NUMBER") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_pk" ON "S1909632"."STUDENT" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index COURSE_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."COURSE_NAME" ON "S1909632"."COURSE" ("NAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index COURSE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."COURSE_PK" ON "S1909632"."COURSE" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index LECTURERS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."LECTURERS_PK" ON "S1909632"."LECTURER" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index MODULE_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."MODULE_PK" ON "S1909632"."MODULE" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index MODULE_CODE_UK
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."MODULE_CODE_UK" ON "S1909632"."MODULE" ("CODE") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_STUDENT_NUMBER_uindex
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_STUDENT_NUMBER_uindex" ON "S1909632"."STUDENT" ("STUDENT_NUMBER") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_pk" ON "S1909632"."STUDENT" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_COURSE_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_COURSE_pk" ON "S1909632"."STUDENT_COURSE" ("COURSE_ID", "STUDENT_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Index STUDENT_MODULE_pk
--------------------------------------------------------

  CREATE UNIQUE INDEX "S1909632"."STUDENT_MODULE_pk" ON "S1909632"."STUDENT_MODULE" ("STUDENT_ID", "MODULE_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT);
--------------------------------------------------------
--  DDL for Procedure P_GET_RSA_CRYPT_SYSDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE EDITIONABLE PROCEDURE "S1909632"."P_GET_RSA_CRYPT_SYSDATE" (
    ext_rsa_mod    IN VARCHAR2,
    ext_rsa_exp    IN VARCHAR2,
    crypt_sysdate OUT RAW,
    m_err_no      OUT NUMBER,
    m_err_txt     OUT VARCHAR2 )
IS BEGIN
    m_err_no := 0;
    crypt_sysdate := f_get_rsa_crypt( ext_rsa_mod, ext_rsa_exp,
        TO_CHAR( CURRENT_TIMESTAMP, 'DY MON DD HH24:MI:SS TZD YYYY' ) );
EXCEPTION
    WHEN OTHERS THEN
        m_err_no := SQLCODE;
        m_err_txt := SQLERRM;
END p_get_rsa_crypt_sysdate;

/
--------------------------------------------------------
--  DDL for Package APP_SEC_PKG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE "S1909632"."APP_SEC_PKG" IS

    -- For Chapter 6 testing only - move to app in later versions of this package
    PROCEDURE p_get_shared_passphrase(
        ext_modulus               VARCHAR2,
        ext_exponent              VARCHAR2,
        secret_pass_salt      OUT RAW,
        secret_pass_count     OUT RAW,
        secret_pass_algorithm OUT RAW,
        secret_pass           OUT RAW,
        m_err_no              OUT NUMBER,
        m_err_txt             OUT VARCHAR2 );

    -- For Chapter 6 testing only - remove in later versions of this package
    PROCEDURE p_get_des_crypt_test_data(
        ext_modulus               VARCHAR2,
        ext_exponent              VARCHAR2,
        secret_pass_salt      OUT RAW,
        secret_pass_count     OUT RAW,
        secret_pass_algorithm OUT RAW,
        secret_pass           OUT RAW,
        m_err_no              OUT NUMBER,
        m_err_txt             OUT VARCHAR2,
        test_data                 VARCHAR2,
        crypt_data            OUT RAW );

    FUNCTION f_get_crypt_secret_pass( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 ) RETURN RAW;

    FUNCTION f_get_crypt_secret_algorithm( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 ) RETURN RAW;

    FUNCTION f_get_crypt_secret_salt( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 ) RETURN RAW;

    FUNCTION f_get_crypt_secret_count( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 ) RETURN RAW;

    FUNCTION f_get_crypt_data( clear_text VARCHAR2 ) RETURN RAW;

    FUNCTION f_get_decrypt_data( crypt_data RAW ) RETURN VARCHAR2;

    -- For Chapter 6 testing only - remove in later versions of this package
    FUNCTION f_show_algorithm RETURN VARCHAR2;

END app_sec_pkg;

/
--------------------------------------------------------
--  DDL for Package Body APP_SEC_PKG
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE PACKAGE BODY "S1909632"."APP_SEC_PKG" IS

    -- testing only
    PROCEDURE p_get_shared_passphrase(
        ext_modulus               VARCHAR2,
        ext_exponent              VARCHAR2,
        secret_pass_salt      OUT RAW,
        secret_pass_count     OUT RAW,
        secret_pass_algorithm OUT RAW,
        secret_pass           OUT RAW,
        m_err_no              OUT NUMBER,
        m_err_txt             OUT VARCHAR2 )
    IS BEGIN
        m_err_no := 0;
        secret_pass_salt := f_get_crypt_secret_salt( ext_modulus, ext_exponent );
        secret_pass_count := f_get_crypt_secret_count( ext_modulus, ext_exponent );
        secret_pass := f_get_crypt_secret_pass( ext_modulus, ext_exponent );
        secret_pass_algorithm :=
            f_get_crypt_secret_algorithm( ext_modulus, ext_exponent );
    EXCEPTION
        WHEN OTHERS THEN
            m_err_no := SQLCODE;
            m_err_txt := SQLERRM;
    END p_get_shared_passphrase;

    -- testing only 
    PROCEDURE p_get_des_crypt_test_data(
        ext_modulus               VARCHAR2,
        ext_exponent              VARCHAR2,
        secret_pass_salt      OUT RAW,
        secret_pass_count     OUT RAW,
        secret_pass_algorithm OUT RAW,
        secret_pass           OUT RAW,
        m_err_no              OUT NUMBER,
        m_err_txt             OUT VARCHAR2,
        test_data                 VARCHAR2,
        crypt_data            OUT RAW )
    IS BEGIN
        m_err_no := 0;
        secret_pass_salt := f_get_crypt_secret_salt( ext_modulus, ext_exponent );
        secret_pass_count := f_get_crypt_secret_count( ext_modulus, ext_exponent );
        secret_pass := f_get_crypt_secret_pass( ext_modulus, ext_exponent );
        secret_pass_algorithm :=
            f_get_crypt_secret_algorithm( ext_modulus, ext_exponent );
        crypt_data := f_get_crypt_data( test_data );
    EXCEPTION
        WHEN OTHERS THEN
            m_err_no := SQLCODE;
            m_err_txt := SQLERRM;
    END p_get_des_crypt_test_data;

    FUNCTION f_get_crypt_secret_pass( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getCryptSessionSecretDESPassPhrase( java.lang.String, java.lang.String ) return oracle.sql.RAW';

    FUNCTION f_get_crypt_secret_algorithm( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getCryptSessionSecretDESAlgorithm( java.lang.String, java.lang.String ) return oracle.sql.RAW';

    FUNCTION f_get_crypt_secret_salt( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getCryptSessionSecretDESSalt( java.lang.String, java.lang.String ) return oracle.sql.RAW';

    FUNCTION f_get_crypt_secret_count( ext_modulus VARCHAR2,
        ext_exponent VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getCryptSessionSecretDESIterationCount( java.lang.String, java.lang.String ) return oracle.sql.RAW';

    FUNCTION f_get_crypt_data( clear_text VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getCryptData( java.lang.String ) return oracle.sql.RAW';

    FUNCTION f_get_decrypt_data( crypt_data RAW )
    RETURN VARCHAR2
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getDecryptData( oracle.sql.RAW ) return java.lang.String';

    -- testing only
    FUNCTION f_show_algorithm 
    RETURN VARCHAR2
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.showAlgorithm() return java.lang.String';

END app_sec_pkg;

/
--------------------------------------------------------
--  DDL for Function F_GET_RSA_CRYPT
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE FUNCTION "S1909632"."F_GET_RSA_CRYPT" (
    ext_rsa_mod VARCHAR2, ext_rsa_exp VARCHAR2, cleartext VARCHAR2 )
    RETURN RAW
    AS LANGUAGE JAVA
    NAME 'OracleJavaSecure.getRSACryptData( java.lang.String, java.lang.String, java.lang.String ) return oracle.sql.RAW';

/
--------------------------------------------------------
--  Constraints for Table COURSE
--------------------------------------------------------

  ALTER TABLE "S1909632"."COURSE" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."COURSE" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."COURSE" ADD CONSTRAINT "COURSE_PK" PRIMARY KEY ("ID")
  USING INDEX "S1909632"."COURSE_PK"  ENABLE;
--------------------------------------------------------
--  Constraints for Table LECTURER
--------------------------------------------------------

  ALTER TABLE "S1909632"."LECTURER" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."LECTURER" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."LECTURER" ADD CONSTRAINT "LECTURERS_PK" PRIMARY KEY ("ID")
  USING INDEX "S1909632"."LECTURERS_PK"  ENABLE;
--------------------------------------------------------
--  Constraints for Table MODULE
--------------------------------------------------------

  ALTER TABLE "S1909632"."MODULE" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("COURSE_ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("NAME" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("CODE" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("SEMESTER" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("CAT_POINTS" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" MODIFY ("LECTURER_ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."MODULE" ADD CONSTRAINT "MODULE_PK" PRIMARY KEY ("ID")
  USING INDEX "S1909632"."MODULE_PK"  ENABLE;
  ALTER TABLE "S1909632"."MODULE" ADD CONSTRAINT "MODULE_CODE_UK" UNIQUE ("CODE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT) ENABLE;
--------------------------------------------------------
--  Constraints for Table STUDENT
--------------------------------------------------------

  ALTER TABLE "S1909632"."STUDENT" MODIFY ("ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT" MODIFY ("FIRST_NAME" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT" MODIFY ("LAST_NAME" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT" MODIFY ("STUDENT_NUMBER" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT" ADD CONSTRAINT "STUDENT_pk" PRIMARY KEY ("ID")
  USING INDEX "S1909632"."STUDENT_pk"  ENABLE;
--------------------------------------------------------
--  Constraints for Table STUDENT_COURSE
--------------------------------------------------------

  ALTER TABLE "S1909632"."STUDENT_COURSE" MODIFY ("STUDENT_ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT_COURSE" MODIFY ("COURSE_ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT_COURSE" ADD CONSTRAINT "STUDENT_COURSE_pk" PRIMARY KEY ("COURSE_ID", "STUDENT_ID")
  USING INDEX "S1909632"."STUDENT_COURSE_pk"  ENABLE;
--------------------------------------------------------
--  Constraints for Table STUDENT_MODULE
--------------------------------------------------------

  ALTER TABLE "S1909632"."STUDENT_MODULE" MODIFY ("STUDENT_ID" NOT NULL ENABLE);
  ALTER TABLE "S1909632"."STUDENT_MODULE" ADD CONSTRAINT "STUDENT_MODULE_pk" PRIMARY KEY ("STUDENT_ID", "MODULE_ID")
  USING INDEX "S1909632"."STUDENT_MODULE_pk"  ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table MODULE
--------------------------------------------------------

  ALTER TABLE "S1909632"."MODULE" ADD CONSTRAINT "MODULE_COURSE_FK" FOREIGN KEY ("COURSE_ID")
	  REFERENCES "S1909632"."COURSE" ("ID") ENABLE;
  ALTER TABLE "S1909632"."MODULE" ADD CONSTRAINT "MODULE___fk" FOREIGN KEY ("LECTURER_ID")
	  REFERENCES "S1909632"."LECTURER" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STUDENT_COURSE
--------------------------------------------------------

  ALTER TABLE "S1909632"."STUDENT_COURSE" ADD CONSTRAINT "STUDENT_COURSE_STUDENT_null_fk" FOREIGN KEY ("STUDENT_ID")
	  REFERENCES "S1909632"."STUDENT" ("ID") ENABLE;
  ALTER TABLE "S1909632"."STUDENT_COURSE" ADD CONSTRAINT "STUDENT_COURSE_COURSE_null_fk" FOREIGN KEY ("COURSE_ID")
	  REFERENCES "S1909632"."COURSE" ("ID") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table STUDENT_MODULE
--------------------------------------------------------

  ALTER TABLE "S1909632"."STUDENT_MODULE" ADD CONSTRAINT "STUDENT_MODULE_STUDENT_null_fk" FOREIGN KEY ("STUDENT_ID")
	  REFERENCES "S1909632"."STUDENT" ("ID") ENABLE;
  ALTER TABLE "S1909632"."STUDENT_MODULE" ADD CONSTRAINT "STUDENT_MODULE_MODULE_null_fk" FOREIGN KEY ("MODULE_ID")
	  REFERENCES "S1909632"."MODULE" ("ID") ENABLE;
