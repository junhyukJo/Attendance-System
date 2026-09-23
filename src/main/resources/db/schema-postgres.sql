CREATE TABLE IF NOT EXISTS members (
    member_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    birth_date DATE,
    grade VARCHAR(10),
    warn NUMERIC(2, 1),
    mileage_score INTEGER NOT NULL DEFAULT 0,
    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    chg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE members ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE members ADD COLUMN IF NOT EXISTS birth_date DATE;
ALTER TABLE members ADD COLUMN IF NOT EXISTS grade VARCHAR(10);
ALTER TABLE members ADD COLUMN IF NOT EXISTS warn NUMERIC(2, 1);
ALTER TABLE members ADD COLUMN IF NOT EXISTS mileage_score INTEGER NOT NULL DEFAULT 0;
ALTER TABLE members ADD COLUMN IF NOT EXISTS reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE members ADD COLUMN IF NOT EXISTS chg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE members ALTER COLUMN birth_date DROP NOT NULL;
ALTER TABLE members ALTER COLUMN grade DROP NOT NULL;
ALTER TABLE members ALTER COLUMN warn DROP NOT NULL;

CREATE TABLE IF NOT EXISTS jeongmos (
    jeongmo_id VARCHAR(50) PRIMARY KEY,
    jeongmo_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    place VARCHAR(120) NOT NULL,
    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    chg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS attendance_records (
    id VARCHAR(50) PRIMARY KEY,
    member_id VARCHAR(50),
    jeongmo_id VARCHAR(50),
    name VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL,
    jeongmo_date DATE,
    jeongmo_start_time TIME,
    jeongmo_end_time TIME,
    jeongmo_place VARCHAR(120),
    reg_dtm TIMESTAMP NOT NULL,
    chg_dtm TIMESTAMP NOT NULL,
    CONSTRAINT fk_attendance_records_member FOREIGN KEY (member_id) REFERENCES members (member_id),
    CONSTRAINT fk_attendance_records_jeongmo FOREIGN KEY (jeongmo_id) REFERENCES jeongmos (jeongmo_id)
);

ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS member_id VARCHAR(50);
ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS jeongmo_id VARCHAR(50);
ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS jeongmo_date DATE;
ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS jeongmo_start_time TIME;
ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS jeongmo_end_time TIME;
ALTER TABLE attendance_records ADD COLUMN IF NOT EXISTS jeongmo_place VARCHAR(120);

CREATE TABLE IF NOT EXISTS jeongmo_required_members (
    jeongmo_id VARCHAR(50) NOT NULL,
    member_id VARCHAR(50) NOT NULL,
    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (jeongmo_id, member_id),
    CONSTRAINT fk_jeongmo_required_members_jeongmo FOREIGN KEY (jeongmo_id) REFERENCES jeongmos (jeongmo_id),
    CONSTRAINT fk_jeongmo_required_members_member FOREIGN KEY (member_id) REFERENCES members (member_id)
);

CREATE TABLE IF NOT EXISTS mileage_history (
    history_id VARCHAR(50) PRIMARY KEY,
    member_id VARCHAR(50) NOT NULL,
    member_name VARCHAR(50) NOT NULL,
    action_type VARCHAR(10) NOT NULL,
    points INTEGER NOT NULL,
    category VARCHAR(100) NOT NULL,
    detail_reason VARCHAR(255),
    before_mileage INTEGER NOT NULL,
    after_mileage INTEGER NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_mileage_history_member FOREIGN KEY (member_id) REFERENCES members (member_id)
);
