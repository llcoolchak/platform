CREATE TABLE AM_SUBSCRIBER (
    SUBSCRIBER_ID NUMBER(10, 0) AUTO_INCREMENT,
    USER_ID VARCHAR2(50) NOT NULL,
    TENANT_ID NUMBER(10, 0) NOT NULL,
    EMAIL_ADDRESS VARCHAR2(256) NULL,
    DATE_SUBSCRIBED DATE NOT NULL,
    PRIMARY KEY (SUBSCRIBER_ID),
    UNIQUE (TENANT_ID,USER_ID)
);

CREATE TABLE AM_APPLICATION (
    APPLICATION_ID NUMBER(10, 0) AUTO_INCREMENT,
    NAME VARCHAR2(100),
    SUBSCRIBER_ID NUMBER(10, 0),
    FOREIGN KEY(SUBSCRIBER_ID) REFERENCES AM_SUBSCRIBER(SUBSCRIBER_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    PRIMARY KEY(APPLICATION_ID),
    UNIQUE (NAME,SUBSCRIBER_ID)
);

CREATE TABLE AM_API (
    API_ID NUMBER(10, 0) AUTO_INCREMENT,
    API_PROVIDER VARCHAR2(256),
    API_NAME VARCHAR2(256),
    API_VERSION VARCHAR2(30),
    CONTEXT VARCHAR2(256),
    PRIMARY KEY(API_ID),
    UNIQUE (API_PROVIDER,API_NAME,API_VERSION)
);

CREATE TABLE AM_SUBSCRIPTION (
    SUBSCRIPTION_ID NUMBER(10, 0) AUTO_INCREMENT,
    TIER_ID VARCHAR2(50),
    API_ID NUMBER(10, 0),
    LAST_ACCESSED DATE NULL,
    APPLICATION_ID NUMBER(10, 0),
    FOREIGN KEY(APPLICATION_ID) REFERENCES AM_APPLICATION(APPLICATION_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    FOREIGN KEY(API_ID) REFERENCES AM_API(API_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    PRIMARY KEY (SUBSCRIPTION_ID)
);

CREATE TABLE AM_SUBSCRIPTION_KEY_MAPPING (
    SUBSCRIPTION_ID NUMBER(10, 0),
    ACCESS_TOKEN VARCHAR2(512),
    KEY_TYPE VARCHAR2(512) NOT NULL,
    FOREIGN KEY(SUBSCRIPTION_ID) REFERENCES AM_SUBSCRIPTION(SUBSCRIPTION_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    PRIMARY KEY(SUBSCRIPTION_ID,ACCESS_TOKEN)
);

CREATE TABLE AM_APPLICATION_KEY_MAPPING (
    APPLICATION_ID NUMBER(10, 0),
    ACCESS_TOKEN VARCHAR2(512),
    KEY_TYPE VARCHAR2(512) NOT NULL,
    FOREIGN KEY(APPLICATION_ID) REFERENCES AM_APPLICATION(APPLICATION_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    PRIMARY KEY(APPLICATION_ID,ACCESS_TOKEN)
);

CREATE TABLE AM_API_LC_EVENT (
    EVENT_ID NUMBER(10, 0) AUTO_INCREMENT,
    API_ID NUMBER(10, 0) NOT NULL,
    PREVIOUS_STATE VARCHAR2(50),
    NEW_STATE VARCHAR2(50) NOT NULL,
    USER_ID VARCHAR2(50) NOT NULL,
    TENANT_ID NUMBER(10, 0) NOT NULL,
    EVENT_DATE DATE NOT NULL,
    FOREIGN KEY(API_ID) REFERENCES AM_API(API_ID) ON UPDATE CASCADE ON DELETE RESTRICT,
    PRIMARY KEY (EVENT_ID)
);

CREATE INDEX IDX_SUB_APP_ID ON AM_SUBSCRIPTION (APPLICATION_ID, SUBSCRIPTION_ID);