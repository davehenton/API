truncate table cwsint.ALLGTN_T immediate;
INSERT INTO cwsint.ALLGTN_T(IDENTIFIER, ABUSE_ENDT, ABUSE_FREQ, ABUSE_PDCD, LOC_DSC, ABUSE_STDT, ALG_DSPC, ALG_TPC, DISPSN_DSC, DISPSN_DT, IJHM_DET_B, NON_PRT_CD, STFADD_IND, LST_UPD_ID, LST_UPD_TS, FKCLIENT_T, FKCLIENT_0, FKREFERL_T, CNTY_SPFCD, ZIPPY_IND, PLC_FCLC)
VALUES ('Aaeae9r0F4', null, 2, 'M', '                                                                           ', null, 0, 2180, '                                                                                                                                                                                                                                                              ', null, 'N', 'N', 'N', '0F4', '1999-07-15 17:06:29.208', 'AHooKwN0F4', null, '8mu1E710F4', '19', 'N', null);

truncate table cwsint.STFPERST immediate;
INSERT INTO cwsint.STFPERST(IDENTIFIER, END_DT, FIRST_NM, JOB_TL_DSC, LAST_NM, MID_INI_NM, NMPRFX_DSC, PHONE_NO, TEL_EXT_NO, START_DT, SUFX_TLDSC, TLCMTR_IND, LST_UPD_ID, LST_UPD_TS, FKCWS_OFFT, AVLOC_DSC, SSRS_WKRID, CNTY_SPFCD, DTYWKR_IND, FKCWSADDRT, EMAIL_ADDR)
VALUES ('q1p', null, 'External Interface  ', 'external interface            ', 'SCXCIN7                  ', ' ', '      ', '9165672100', '0', '1999-10-06', '    ', 'N', 'q38', '2000-08-29 13:51:39.247824', 'MIZN02k00E', '                                                                                                                                                                ', '    ', '99', 'N', '3XPCP92q38', null);

truncate table cwsint.REFR_CLT immediate;
INSERT INTO cwsInt.REFR_CLT(APRVL_NO, APV_STC, DSP_RSNC, DISPSTN_CD, RCL_DISPDT, SLFRPT_IND, STFADD_IND, LST_UPD_ID, LST_UPD_TS, FKCLIENT_T, FKREFERL_T, DSP_CLSDSC, RFCL_AGENO, AGE_PRD_CD, CNTY_SPFCD, MHLTH_IND, ALCHL_IND, DRUG_IND) 
VALUES ('86XV1bG06s', 122, 681, 'S', '1998-07-21', 'N', 'N', '06s', '1998-08-04 13:07:52.600555', 'AazXkWY06s', 'LNuzMKw06s', '                                                                                                                                                                                                                                                              ', 2, 'Y', '43', null, null, null);

truncate table cwsint.REFERL_T immediate;
INSERT INTO cwsInt.REFERL_T(IDENTIFIER, ADD_INF_CD, ANRPTR_IND, PETAPL_IND, APRVL_NO, APV_STC, CR_PERP_CD, REFCLSR_DT, CMM_MTHC, CHILOC_TXT, ALGDSC_DOC, ER_REF_DOC, INVSTG_DOC, XRPT_LWIND, FMY_AW_IND, GVR_ENTC, LGL_DEF_CD, LGLRGT_IND, LMT_ACSSCD, XRPT_RCVDT, REFERRL_NM, ADQT_CS_CD, REF_RCV_DT, REF_RCV_TM, RFR_RSPC, RFRD_RSC, RSP_DTR_DT, RSP_DTR_TM, RSP_RTNTXT, SCN_NT_TXT, SP_INCL_CD, SFC_INF_CD, UNFD_SR_CD, LST_UPD_ID, LST_UPD_TS, FKREFERL_T, FKADDRS_T, FKSTFPERS0, FKSTFPERST, CNTY_SPFCD, SPRJRF_IND, ZIPPY_IND, HOMLES_IND, FAMREF_IND, FIRST_EODT, RSP_AGY_CD, L_GVR_ENTC, LMT_ACS_DT, LMT_ACSDSC, ORIGCLS_DT) 
VALUES ('AbiQCgu0Hj', ' ', 'N', 'N', 'D5YRVOm0Hj', 122, ' ', '2000-03-03', 409, null, null, 'L3H7sSC0Hj', null, 'N', 'N', 1118, ' ', 'N', 'N', null, 'Verification (R3)                  ', ' ', '2000-01-31', '14:44:00', 1520, 0, '2000-01-31', '14:46:00', null, null, ' ', ' ', ' ', '0Hj', '2000-03-03 09:59:40.009312', null, null, '0Hj', '0Hj', '51', 'N', 'N', 'N', 'N', null, 'C', null, null, null, '2000-03-03');

truncate table cwsint.REPTR_T immediate;
INSERT INTO cwsInt.REPTR_T(RPTR_BDGNO, RPTR_CTYNM, COL_RELC, CMM_MTHC, CNFWVR_IND, FDBACK_DOC, RPTR_EMPNM, FEEDBCK_DT, FB_RQR_IND, RPTR_FSTNM, RPTR_LSTNM, MNRPTR_IND, MSG_EXT_NO, MSG_TEL_NO, MID_INI_NM, NMPRFX_DSC, PRM_TEL_NO, PRM_EXT_NO, STATE_C, RPTR_ST_NM, RPTR_ST_NO, SUFX_TLDSC, RPTR_ZIPNO, LST_UPD_ID, LST_UPD_TS, FKREFERL_T, FKLAW_ENFT, ZIP_SFX_NO, CNTY_SPFCD) 
VALUES ('      ', 'City                ', 591, 0, 'N', null, '                                   ', null, 'N', 'Fred                ', 'Reporter                 ', 'N', 0, 0, ' ', '      ', 0, 0, 1828, 'Street                                  ', '12345     ', '    ', 95845, '0Hj', '2000-01-31 14:44:57.709335', 'AbiQCgu0Hj', null, 0, '51');

truncate table cwsint.CLIENT_T immediate;
INSERT INTO cwsInt.CLIENT_T(IDENTIFIER, ADPTN_STCD, ALN_REG_NO, BIRTH_DT, BR_FAC_NM, B_STATE_C, B_CNTRY_C,	CHLD_CLT_B,	COM_FST_NM,	COM_LST_NM, COM_MID_NM, CONF_EFIND, CONF_ACTDT,	CREATN_DT, DEATH_DT, DTH_RN_TXT, DRV_LIC_NO,   	D_STATE_C, GENDER_CD, I_CNTRY_C, IMGT_STC, INCAPC_CD, LITRATE_CD, MAR_HIST_B, MRTL_STC, MILT_STACD, NMPRFX_DSC, NAME_TPC, OUTWRT_IND, P_ETHNCTYC, P_LANG_TPC, RLGN_TPC, S_LANG_TC, SENSTV_IND, SNTV_HLIND, SS_NO, SSN_CHG_CD, 	SUFX_TLDSC,	UNEMPLY_CD,	LST_UPD_ID,	LST_UPD_TS,	COMMNT_DSC, EST_DOB_CD,	BP_VER_IND,	HISP_CD, CURRCA_IND, CURREG_IND, COTH_DESC, PREVCA_IND,	PREREG_IND,	POTH_DESC, HCARE_IND, LIMIT_IND, BIRTH_CITY, HEALTH_TXT, MTERM_DT, FTERM_DT, ZIPPY_IND, DEATH_PLC, TR_MBVRT_B, TRBA_CLT_B, SOC158_IND, DTH_DT_IND, EMAIL_ADDR, ADJDEL_IND, ETH_UD_CD, HISP_UD_CD, SOCPLC_CD, CL_INDX_NO)
VALUES ('AapJGAU04Z', 'N', ' ',	'1995-03-31', ' ',	0,	0,	'N', 'Alexander', 'Zamboanga', ' ',	'N', NULL,	'1998-05-30', NULL, NULL, ' ',	0,	'M', 0,	0, 'U', 'U', 'N', 0, 'U', ' ', 1313, 'N', 0, 0,	0,	0, 'N', 'N',	'520565309', 'O', '90',	'U ', '04Z', '1998-05-30 14:18:33.65434', ' ', 'N',	'N', 'U', 'N', 'N',	' ', 'N', 'N', ' ',	'N', 'N', ' ',	NULL, NULL,	NULL, 'N', NULL, 'N', 'N', 'N',	'N', NULL, NULL, NULL, NULL, 'N', NULL);

truncate table cwsint.CLIENT_T immediate;
INSERT INTO cwsInt.CLIENT_T(IDENTIFIER, ADPTN_STCD, ALN_REG_NO, BIRTH_DT, BR_FAC_NM, B_STATE_C, B_CNTRY_C,	CHLD_CLT_B,	COM_FST_NM,	COM_LST_NM, COM_MID_NM, CONF_EFIND, CONF_ACTDT,	CREATN_DT, DEATH_DT, DTH_RN_TXT, DRV_LIC_NO,   	D_STATE_C, GENDER_CD, I_CNTRY_C, IMGT_STC, INCAPC_CD, LITRATE_CD, MAR_HIST_B, MRTL_STC, MILT_STACD, NMPRFX_DSC, NAME_TPC, OUTWRT_IND, P_ETHNCTYC, P_LANG_TPC, RLGN_TPC, S_LANG_TC, SENSTV_IND, SNTV_HLIND, SS_NO, SSN_CHG_CD, 	SUFX_TLDSC,	UNEMPLY_CD,	LST_UPD_ID,	LST_UPD_TS,	COMMNT_DSC, EST_DOB_CD,	BP_VER_IND,	HISP_CD, CURRCA_IND, CURREG_IND, COTH_DESC, PREVCA_IND,	PREREG_IND,	POTH_DESC, HCARE_IND, LIMIT_IND, BIRTH_CITY, HEALTH_TXT, MTERM_DT, FTERM_DT, ZIPPY_IND, DEATH_PLC, TR_MBVRT_B, TRBA_CLT_B, SOC158_IND, DTH_DT_IND, EMAIL_ADDR, ADJDEL_IND, ETH_UD_CD, HISP_UD_CD, SOCPLC_CD, CL_INDX_NO)
VALUES ('AaiU7IW0Rt', 'N', ' ', '1972-08-17', ' ', 0, 0, 'N', 'Tumbling', 'Waters', ' ',  'N',	NULL, '2004-08-17', NULL, NULL,	' ', 0,	'M', 0,	0, 'U',	'U', 'N', 0, 'U', ' ', 1313, 'N', 0, 0,	0, 0, 'N', 'N',	' ', 'O', ' ', 'U', '0Rt',	'2004-08-17 08:39:04.891566', ' ', 'Y',	'N', 'U', 'N', 'N', ' ', 'N', 'N', ' ',	'N', 'N', ' ',	NULL,	NULL,	NULL, 'N', NULL, 'N', 'N', 'N',	'N', NULL, NULL, NULL, NULL, 'N', NULL),
('AapJGAU04Z', 'N', ' ',	'1995-03-31', ' ',	0,	0,	'N', 'Alexander', 'Zamboanga', ' ',	'N', NULL,	'1998-05-30', NULL, NULL, ' ',	0,	'M', 0,	0, 'U', 'U', 'N', 0, 'U', ' ', 1313, 'N', 0, 0,	0,	0, 'N', 'N', '520565309', 'O', '90', 'U ', '04Z', '1998-05-30 14:18:33.65434', ' ', 'N',	'N', 'U', 'N', 'N', ' ', 'N', 'N', ' ',	'N', 'N', ' ',	NULL, NULL, NULL, 'N', NULL, 'N', 'N', 'N', 'N', NULL, NULL, NULL, NULL, 'N', NULL);

truncate table cwsint.ATTRNY_T immediate;
INSERT INTO cwsInt.ATTRNY_T (IDENTIFIER, CITY_NM, CWATRY_IND, FAX_NO, FIRST_NM, GVR_ENTC, LANG_TPC, LAST_NM, MSG_EXT_NO, MSG_TEL_NO, MID_INI_NM, NMPRFX_DSC, POSTIL_DSC, PRM_EXT_NO, PRM_TEL_NO, STATE_C, STREET_NM, STREET_NO, SUFX_TLDSC, ZIP_NO, LST_UPD_ID, LST_UPD_TS, BUSNSS_NM, ZIP_SFX_NO, END_DT, ARCASS_IND, EMAIL_ADDR)
VALUES ('AcjOOPa0BU',' ',' ',0,'Lance',0,1274,'Johnson',0,0,' ',' ','Attorney',0,0,0,' ',' ',' ',0,'0BU','1998-07-30 09:14:09.412017',' ',0,NULL,'N',NULL),
('AcoWleS0Rx',' ',' ',0,'Jose',0,0,'Perez',0,0,	' ',' ',' ',0,0,0,' ',' ',' ',0,'0Rx','2004-04-27 12:28:08.960772',' ',0,NULL,'N',NULL);

-- Gregg Hill: Adding rows for Other Client Names
INSERT INTO CWSINT.OCL_NM_T
	(THIRD_ID, FIRST_NM, LAST_NM, MIDDLE_NM, NMPRFX_DSC, NAME_TPC, SUFX_TLDSC, LST_UPD_ID, LST_UPD_TS, FKCLIENT_T)
VALUES 
	('123', 'Gregg', 'Hill', 'Brian', '1',1 , '1', '1', '1999-01-31 14:44:57.709335' , '1');
	
INSERT INTO CWSINT.OCL_NM_T
	(THIRD_ID, FIRST_NM, LAST_NM, MIDDLE_NM, NMPRFX_DSC, NAME_TPC, SUFX_TLDSC, LST_UPD_ID, LST_UPD_TS, FKCLIENT_T)
VALUES 
	('124', 'Billiam', 'Hill', 'Brian', '1',1 , '1', '1', '2000-01-31 14:44:57.709335' , '1');	

