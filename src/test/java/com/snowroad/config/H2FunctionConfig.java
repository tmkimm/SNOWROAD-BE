package com.snowroad.config;


import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Configuration
@ActiveProfiles("test")  // 선택: test 프로파일일 때만 실행하고 싶으면
public class H2FunctionConfig {

    @PostConstruct
    public void registerStrToDate() {
        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "")) {
            Statement stmt = conn.createStatement();
            stmt.execute(
                    "CREATE ALIAS IF NOT EXISTS STR_TO_DATE FOR \"com.snowroad.util.H2Functions.strToDate\""
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
