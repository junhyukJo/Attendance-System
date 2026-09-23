CREATE TABLE IF NOT EXISTS members (
    member_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    birth_date DATE,
    grade VARCHAR(10),
    warn NUMERIC(2, 1),
    mileage_score INTEGER NOT NULL DEFAULT 0,
    reg_dtm TIMESTAMP NOT NULL,
    chg_dtm TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS jeongmos (
    jeongmo_id VARCHAR(50) PRIMARY KEY,
    jeongmo_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    place VARCHAR(120) NOT NULL,
    reg_dtm TIMESTAMP NOT NULL,
    chg_dtm TIMESTAMP NOT NULL
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
    CONSTRAINT fk_test_attendance_member FOREIGN KEY (member_id) REFERENCES members (member_id),
    CONSTRAINT fk_test_attendance_jeongmo FOREIGN KEY (jeongmo_id) REFERENCES jeongmos (jeongmo_id)
);

CREATE TABLE IF NOT EXISTS jeongmo_required_members (
    jeongmo_id VARCHAR(50) NOT NULL,
    member_id VARCHAR(50) NOT NULL,
    reg_dtm TIMESTAMP NOT NULL,
    PRIMARY KEY (jeongmo_id, member_id),
    CONSTRAINT fk_test_jeongmo_required_jeongmo FOREIGN KEY (jeongmo_id) REFERENCES jeongmos (jeongmo_id),
    CONSTRAINT fk_test_jeongmo_required_member FOREIGN KEY (member_id) REFERENCES members (member_id)
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
    reg_dtm TIMESTAMP NOT NULL
);
