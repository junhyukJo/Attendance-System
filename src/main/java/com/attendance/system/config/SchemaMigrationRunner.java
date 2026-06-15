package com.attendance.system.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SchemaMigrationRunner implements ApplicationRunner {

	private final JdbcTemplate jdbcTemplate;

	public SchemaMigrationRunner(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void run(ApplicationArguments args) {
		renameLegacyMemberIdColumn();
		ensureMemberColumns();
		ensureAttendanceMemberColumn();
	}

	private void renameLegacyMemberIdColumn() {
		if (columnExists("members", "id") && !columnExists("members", "member_id")) {
			jdbcTemplate.execute("ALTER TABLE members RENAME COLUMN id TO member_id");
		}
	}

	private void ensureMemberColumns() {
		executeIfMissing("members", "active", "ALTER TABLE members ADD COLUMN active BOOLEAN NOT NULL DEFAULT TRUE");
		executeIfMissing("members", "birth_date", "ALTER TABLE members ADD COLUMN birth_date DATE");
		executeIfMissing("members", "grade", "ALTER TABLE members ADD COLUMN grade VARCHAR(10)");
		executeIfMissing("members", "warn", "ALTER TABLE members ADD COLUMN warn NUMERIC(2, 1)");
		executeIfMissing("members", "mileage_score", "ALTER TABLE members ADD COLUMN mileage_score INTEGER NOT NULL DEFAULT 0");
		executeIfMissing("members", "reg_dtm", "ALTER TABLE members ADD COLUMN reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
		executeIfMissing("members", "chg_dtm", "ALTER TABLE members ADD COLUMN chg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
		jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN grade DROP NOT NULL");
		jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN warn DROP NOT NULL");
		jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN birth_date DROP NOT NULL");
	}

	private void ensureAttendanceMemberColumn() {
		executeIfMissing("attendance_records", "member_id", "ALTER TABLE attendance_records ADD COLUMN member_id VARCHAR(50)");
	}

	private void executeIfMissing(String tableName, String columnName, String sql) {
		if (!columnExists(tableName, columnName)) {
			jdbcTemplate.execute(sql);
		}
	}

	private boolean columnExists(String tableName, String columnName) {
		Integer count = jdbcTemplate.queryForObject(
				"""
				SELECT COUNT(*)
				FROM information_schema.columns
				WHERE UPPER(table_name) = UPPER(?)
				  AND UPPER(column_name) = UPPER(?)
				""",
				Integer.class,
				tableName,
				columnName
		);
		return count != null && count > 0;
	}
}
