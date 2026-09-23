package com.attendance.system.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
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
		ensureJeongmoTables();
		ensureAttendanceMemberColumn();
		removeGarbageMembers();
		seedMembers();
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
		executeIfMissing("attendance_records", "jeongmo_id", "ALTER TABLE attendance_records ADD COLUMN jeongmo_id VARCHAR(50)");
		executeIfMissing("attendance_records", "jeongmo_date", "ALTER TABLE attendance_records ADD COLUMN jeongmo_date DATE");
		executeIfMissing("attendance_records", "jeongmo_start_time", "ALTER TABLE attendance_records ADD COLUMN jeongmo_start_time TIME");
		executeIfMissing("attendance_records", "jeongmo_end_time", "ALTER TABLE attendance_records ADD COLUMN jeongmo_end_time TIME");
		executeIfMissing("attendance_records", "jeongmo_place", "ALTER TABLE attendance_records ADD COLUMN jeongmo_place VARCHAR(120)");
	}

	private void ensureJeongmoTables() {
		jdbcTemplate.execute(
				"""
				CREATE TABLE IF NOT EXISTS jeongmos (
				    jeongmo_id VARCHAR(50) PRIMARY KEY,
				    jeongmo_date DATE NOT NULL,
				    start_time TIME NOT NULL,
				    end_time TIME NOT NULL,
				    place VARCHAR(120) NOT NULL,
				    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
				    chg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
				)
				"""
		);
		jdbcTemplate.execute(
				"""
				CREATE TABLE IF NOT EXISTS jeongmo_required_members (
				    jeongmo_id VARCHAR(50) NOT NULL,
				    member_id VARCHAR(50) NOT NULL,
				    reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
				    PRIMARY KEY (jeongmo_id, member_id)
				)
				"""
		);
		executeIfMissing("jeongmo_required_members", "reg_dtm", "ALTER TABLE jeongmo_required_members ADD COLUMN reg_dtm TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
	}

	private void removeGarbageMembers() {
		jdbcTemplate.update("DELETE FROM mileage_history WHERE member_id LIKE 'U4%'");
		jdbcTemplate.update("DELETE FROM attendance_records WHERE member_id LIKE 'U4%'");
		jdbcTemplate.update("DELETE FROM members WHERE member_id LIKE 'U4%'");
	}

	private void seedMembers() {
		if (!isPostgreSql()) {
			return;
		}
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("db/data-members.sql"));
		populator.execute(jdbcTemplate.getDataSource());
	}

	private boolean isPostgreSql() {
		String databaseProductName = jdbcTemplate.execute((ConnectionCallback<String>) connection -> connection.getMetaData().getDatabaseProductName());
		return databaseProductName != null && databaseProductName.toLowerCase().contains("postgresql");
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
