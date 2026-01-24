# 🌦 Weather Comfort Index Application

A Spring Boot backend application that integrates with the OpenWeatherMap API to calculate a **Weather Comfort Index**, rank cities based on comfort level, and provide **cached current weather and forecast data** for frontend applications. (<a href="https://github.com/DilmikaSahan/Weather-App-Frontend">Weather App Frontend</a>)

---

## 🚀 Features

- Fetch real-time weather data by city
- Calculate a human-friendly **Comfort Index (0–100)**
- Rank cities by comfort score
- 5-day / 3-hour weather forecast support
- High-performance caching using **Caffeine**
- Cache statistics endpoint for monitoring
- Clean, domain-driven service design

---

## ⚙️ Setup Instructions

### Prerequisites
- Java 21 or higher
- Spring Boot 4.0.1
- Maven
- OpenWeatherMap API key
  
### Tech Stack
- Java 21
- Spring Boot 4.0.1
- Spring WebFlux (`WebClient`)
- Spring Cache
- Caffeine Cache
- OpenWeatherMap API
- Maven
- JUnit 5
- Auth0
---
### Clone the Repository
```bash
git clone https://github.com/your-username/weather-comfort-app.git
cd weather-comfort-app
```
---
### Configuration
```bash
# AUTH0 CONFIGURATION
auth0.audience=YOUR_AUTH0_audience
auth0.issuer=YOUR_AUTH0_issuer

# OPENWEATHERMAP API
openweathermap.api.key=YOUR_API_KEY
openweathermap.api.base-url=https://api.openweathermap.org/data/2.5
```
---
### Run
```bash
mvn spring-boot:run
```
Backend will start on:
```bash
http://localhost:8080
```
---
## Comfort Index Formula Explanation
The Comfort Index represents how comfortable the weather feels to an average person on a 0–100 scale.
| Parameter   | Ideal Range / Value |
| ----------- | ------------------- |
| Temperature | 22.2°C                |
| Humidity    | 30% – 60%           |
| Wind Speed  | 1.5 – 4.0 m/s       |
| Cloudiness  | 30% – 50%           |

Each parameter is normalized to a score between 0 and 100, where:
100 = ideal condition
Lower scores indicate deviation from comfort

 ## Formular of Comfort Index Score
 ## If these conditions are true score will be 100 for each parameter
      Temperature: 22.2°C, score = 100
      Humidity : in range 30% - 60%, score = 100
      WindSpeed : in range 1.5 - 4.0, score = 100
      Cloudiness : in range 30% - 50%, score = 100

calculate the absolute difference (d) for each parameter

  d = |V<sub>actual</sub> - V<sub>ideal</sub>|
  
    (Note: If the value is within a range, d = 0)
score = Σ [(100-(d<sub>i</sub> × p<sub>i</sub>)) × w <sub>i</sub>]

    Pi values,
       Temperature - 4
       Humidity - 2.5
       WindSpeed - 15
       Cloudiness - 1.5

      These Pi value used for distribution Normalization

score = (100 - Td) Tw + (100 - Hd) Hw + (100 - Wd) Ww + (100 - Cd) Cw

    Td - Temperature difference
    Hd - Humidity difference
    Wd - WindSpeed difference
    Cd - Cloudiness difference
    Tw - temp Weight
    Hw - humidity Weight
    Ww - wind Weight
    Cw - cloud Weight


---
## Reasoning Behind Variable Weights

| Factor      | Weight | Reasoning                                    |
| ----------- | ------ | -------------------------------------------- |
| Temperature | 0.4    | Primary driver of human comfort              |
| Humidity    | 0.3    | Strongly affects perceived heat              |
| Wind Speed  | 0.2    | Influences cooling and discomfort            |
| Cloudiness  | 0.1    | Visual and thermal impact, but less critical |

Penalty (P) - Distribution normalizer 

    The penalty is there because different weather metrics use different units and scales. 
    Ex - A change of 5 m/s in wind speed is a massive weather event, but a change of 5\% in humidity is barely noticeable.
Weight (w) - Impact of a Unit Change 

    Once all parameters are on the same 0–100 scale, the weight defines the hierarchy of human experience.
    EX - Even if humidity and temperature both drop by 10 points on their normalized scales, temperature should still 
    affect the final index more because it is more critical to human survival and comfort.

## Why this distribution?

- Temperature and humidity dominate physical comfort
- Wind enhances or reduces discomfort but is secondary
- Cloudiness affects comfort perception, not core physiology
  
      These weights are configurable via properties, allowing easy tuning without code changes.
## Testing & Validation
To ensure the Comfort Index remains reliable across diverse weather scenarios, a comprehensive JUnit 5 suite was implemented. The tests validate the sensitivity of the penalty multipliers (P<sub>i</sub>) and the influence of the variable weights (W<sub>i</sub>).

Test Scenarios Covered
  - **Ideal Conditions :** Verifies that the calculator returns a near-perfect score (≥ 90) when inputs fall within the optimal ranges.
  - **Extreme Stress :** Validates that extreme heat ($45^\circ\text{C}$) or extreme cold ($-10^\circ\text{C}$) triggers the penalty logic to drop the score below 50.
  - **Parameter Sensitivity :** Confirms that individual factors like high humidity or strong winds independently degrade the score, even if other factors are ideal.
  - **Edge Case Resilience :** Ensures the mathematical model handles zero or negative inputs gracefully without producing negative index scores.

Unit Test Snippet

The following test suite uses ReflectionTestUtils to inject configuration weights, simulating a production environment
```bash
@Test
void shouldReturnHighScoreForIdealConditions(){
    // Near 22.2°C, 30-60% Humidity, 1.5-4m/s Wind, 30-50% Clouds
    double score = calculator.calculateComfortIndex(22, 45, 3, 40);
    assertTrue(score >= 90, "Comfort score should be high for ideal weather");
}

@Test
void shouldReturnLowScoreForExtremeHeat() {
    // Penalty logic (Td * 4) should heavily impact the 0.4 temperature weight
    double score = calculator.calculateComfortIndex(45, 80, 1, 10);
    assertTrue(score < 50, "Comfort score should be low for extreme temperature");
}

```
---
### ⚖️ Trade-offs 

1. In-Memory (Caffeine) vs. Distributed Cache (Redis)
  - **Decision**: Used Caffeine for local in-memory caching.
  - **Trade-off**: I sacrificed horizontal scalability (data consistency across multiple server instances) for reduced infrastructure complexity and lower latency.
  - **Reasoning**: For a small-to-medium project, a distributed cache like Redis adds unnecessary network overhead and maintenance costs. Local caching provides the fastest possible lookups for a single-node deployment.

2. Heuristic Formula vs. Biometeorological Models (UTCI/Heat Index)
  - **Decision**: Implemented a custom heuristic formula using Penalty Factors and Weights.
  - **Trade-off**: I sacrificed scientific/medical precision for explainability and tuneability.
  - **Reasoning**: Complex models like the Universal Thermal Climate Index (UTCI) are "black boxes" that are hard to debug or adjust. My approach allows for instant tuning of comfort parameters via configuration without deep meteorological knowledge.

3. State-in-Cache vs. Database Persistence
  - **Decision**: Relying entirely on the cache provider without a backing Database (RDBMS/NoSQL).
  - **Trade-off**: I sacrificed data durability (data is lost on restart) for high performance and a smaller code footprint.
  - **Reasoning**: Weather data is inherently transient. By skipping a database, I removed the need for migrations (Flyway/Liquibase) and boilerplate JPA/Hibernate code, focusing the complexity on the calculation logic instead.

4. Blocking I/O vs. Fully Reactive WebFlux
  - **Decision**: Used RestTemplate (and blocking WebClient calls) and Domain-Driven Services.
  - **Trade-off**: I sacrificed maximum concurrent throughput for code readability and faster development.
  - **Reasoning**: While a fully non-blocking reactive chain handles higher concurrent traffic, it increases development complexity and requires careful consideration of library compatibility (e.g., with synchronous caching annotations). For this project’s scale, the resource overhead of a blocking model is negligible compared to the significant gains in maintainability and simplicity.

5. Domain-Driven Isolation vs. Shared Models
  - **Decision**: Separated "Current Weather" and "Forecast" into distinct service domains.
  - **Trade-off**: Increased the number of classes and boilerplate (DTO mapping).
  - **Reasoning**: This prevents "leaky abstractions" where changes in the forecast API might break the current weather logic. It also allows for independent cache TTLs, as forecast data doesn't need to be refreshed as often as current conditions.

---

## Cache design

Cache Technology
  - Caffeine Cache
  - In-memory, high-performance, thread-safe

| Cache Name          | Purpose                  | TTL       |
| ------------------- | ------------------------ | --------- |
| `weatherCache`      | Current weather per city | 5 minutes |
| `comfortIndexCache` | Ranked comfort results   | 5 minutes |
| `forecastCache`     | 5-day / 3-hour forecast  | 3 hours   |

## Why Different TTLs?
    Current weather changes frequently
    Forecast data updates every 3 hours
    Separate cache managers ensure correct expiration

## Cache Flow
    Request arrives
          ↓
    Cache checked using city code as key
          ↓
    Cache hit → return data
    Cache miss → call OpenWeather API → store result → return

## Known Limitations

  - Comfort Index is heuristic, not medically validated
  - Assumes an “average person” (no personalization)
  - No persistence layer (in-memory cache only)
  - Weather API failures propagate as runtime exceptions
  - Forecast grouping uses API-provided timestamps (UTC-based)



  


