

CREATE TABLE users (
    id VARCHAR2(36 CHAR) NOT NULL,
    username VARCHAR2(100 CHAR) NOT NULL,
    password VARCHAR2(200 CHAR) NOT NULL,
    role VARCHAR2(50 CHAR) DEFAULT 'USER' NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username)
);

CREATE TABLE tasks (
    id VARCHAR2(36 CHAR) NOT NULL,
    title VARCHAR2(255 CHAR) NOT NULL,
    description CLOB,
    status VARCHAR2(20 CHAR) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by VARCHAR2(100 CHAR),
    updated_by VARCHAR2(100 CHAR),
    CONSTRAINT pk_tasks PRIMARY KEY (id)
);


CREATE INDEX idx_tasks_status ON tasks (status);
