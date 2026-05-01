package com.cwm.studentmanagement.perf;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

/**
 * Gatling performance test simulation for the Student Management application.
 * Tests the homepage, dashboard, and student listing endpoints.
 * 
 * To run:
 *   mvn gatling:test -Dgatling.simulationClass=com.cwm.studentmanagement.perf.StudentManagementSimulation
 */
public class StudentManagementSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .acceptHeader("text/html,application/xhtml+xml")
            .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

    ScenarioBuilder scenario = scenario("Student Management Load Test")
            .exec(http("Homepage")
                    .get("/")
                    .check(status().is(200)))
            .pause(1)
            .exec(http("Dashboard")
                    .get("/dashboard")
                    .check(status().in(200, 302))) // 302 if redirected to login
            .pause(1)
            .exec(http("Students List")
                    .get("/students")
                    .check(status().in(200, 302)))
            .pause(1)
            .exec(http("Courses List")
                    .get("/courses")
                    .check(status().in(200, 302)));

    {
        setUp(
                scenario.injectOpen(
                        rampUsers(10).during(30), // Ramp to 10 users over 30 seconds
                        constantUsersPerSec(5).during(60) // Maintain 5 new users/sec for 60 seconds
                )
        )
                .protocols(httpProtocol)
                .assertions(
                        global().responseTime().max().lt(5000), // Max response time < 5s
                        global().successfulRequests().percent().gt(95.0) // 95%+ success rate
                );
    }
}
