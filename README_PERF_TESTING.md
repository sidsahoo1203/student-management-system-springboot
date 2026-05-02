Performance & Load Testing

## Gatling Setup

The project includes **Gatling** for load testing and performance benchmarking.

### Running load tests locally

Prerequisites: App must be running on `http://localhost:8080`

```bash
# Start the app first
mvn -DskipTests package
java -jar web/target/*.jar &

# Run the Gatling simulation
mvn -DskipTests gatling:test -Dgatling.simulationClass=com.cwm.studentmanagement.perf.StudentManagementSimulation

# View the generated report
open target/gatling/studentmanagementsimulation-*/index.html
```

### Test Simulation

**StudentManagementSimulation.java** tests:

- Homepage load
- Dashboard access (handles 200/302 redirects)
- Students list endpoint
- Courses list endpoint

### Load Profile

- **Ramp-up**: 10 users over 30 seconds
- **Sustained load**: 5 new users/second for 60 seconds
- **Assertions**:
  - Max response time < 5 seconds
  - > 95% successful requests

### Interpreting Results

Gatling generates detailed reports in `target/gatling/<simulation-name>/`:

- **index.html**: Main dashboard with response times, throughput, errors
- **global_stats.json**: Machine-readable metrics
- **simulation.log**: Raw event log

Key metrics to monitor:

- **95th percentile response time**: P95 latency
- **Max response time**: Worst-case scenario
- **Success rate**: Percentage of non-error responses
- **Throughput (req/s)**: Requests per second sustained

### Customizing the simulation

Edit [StudentManagementSimulation.java](src/test/java/com/cwm/studentmanagement/perf/StudentManagementSimulation.java):

```java
// Change base URL
.baseUrl("http://staging-server.example.com")

// Adjust load
rampUsers(50).during(60),  // More aggressive ramp
constantUsersPerSec(10).during(120)  // Higher sustained load

// Add more endpoints
.exec(http("API Endpoint")
    .get("/api/students")
    .check(status().is(200)))
```

---

Generated: May 1, 2026
