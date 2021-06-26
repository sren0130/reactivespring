How to run the JAR file

>java -jar -Dspring.profiles.active=dev build/libs/learn-reactivespring-0.0.1-SNAPSHOT.jar

# here the profile is for dev, snapshot jar file is built and saved in build/libs/ directory.


After I commented out the @RestController for FluxAndMonController, I could run the app without problem.

only one RestController is allowed.


When I run the application, I can see that it connects the standalone mongodb.

But I couldn't run JUnit test, because it uses embedded mongo db.

There is a way to disable

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration")
public class DoNotStartMongoTest {
    //...

    @Test
    public void test() {
    }


To use embedded Mongodb in unit test

@Retention(RUNTIME)
@Target(TYPE)
@Import(EmbeddedMongoAutoConfiguration.class)
public @interface EnableEmbeddedMongo {
}

To exclude from production code

@EnableMongoRepositories
@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class MySpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}



Spring boot starter test brings in Junit 4, AssertJ, Hamcrest, Mockito, JSONassert and JsonPath dependencies.

URL: https://howtodoinjava.com/spring-boot2/testing/testing-support/

To use JUnit 5, we need to exclude Junit4.
dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>

    <!-- exclude junit 4 -->
    <exclusions>
        <exclusion>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </exclusion>
    </exclusions>

</dependency>

<!-- junit 5 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>

 <!-- Junit 5 ends  -->

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
    <version>1.4.194</version>
</dependency>


Test Runner
===========

@RunWith(SpringRunner.class)        // Junit 4

@RunWith(MockitoJUnitRunner.class)      // Junit 4 and Mockito

@RunWith(MockitoJUnitRunner.class)
public class EmployeeRestControllerTest
{
    @Mock
    private Repository repository;
}


Junit 5

// @ExtendWith(SpringExtension.class)          // this also include in @WebFluxTTest
@WebFluxTest(controllers = EmployeeController.class)
@Import(EmployeeService.class)
public class EmployeeControllerTest
{
    //
}


@ExtendWith(MockitoExtension.class)         // JUnit 5 & Mockito


Test related Annotations               <==== Most of time is for integration test.
=======================
1. @SpringBootTest
    it helps in writing integration tests. It starts the embeded server and fully initializes the application context.
    There are some details, not sure how it works

2. @WebMvcTest
WebMvcTest(EmployeeRESTController.class)
public class TestEmployeeRESTController {

    @Autowired
    private MockMvc mvc;

    //
}


@WebFluxTest
    This annotation disables full auto-configuration and instead apply only configuration relevant to WEbFlux tests.
    By default, tests annotated with @WebFluxTest will also auto-configure a WebTestClient.

 @WebFluxTest(controllers = EmployeeController.class)
 @Import(EmployeeService.class)
 public class EmployeeControllerTest
 {
     @MockBean
     EmployeeRepository repository;

     @Autowired
     private WebTestClient webClient;

     //tests
 }
