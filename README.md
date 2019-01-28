## 프로젝트 Document
[document.md](document.md)

## IOC(Inversion Of Control)
> 제어의 역전  
  
보통 아래와 같이 자신의 제어권을 자기 자신이 가짐  
```java
class OwnerController {
  private OwnerRepository repository = new OwnerRepository();
}
```
  
> 제어의 역전은 아래와 같이 제어권이 자기 자신에 있지 않고 외부에 있는 것을 말함  
> 실제 의존성 주입은 스프링(ApplicationContext)가 해줌  
> 테스트 코드를 작성하기가 훨씬 쉬움  
```java
class OwnerController {
  private OwnerRepository repo;

  public OwnerController(OwnerRepository repo) {
    this.repo = repo;
  }

  //repo를 사용
}

class OwnerControllerTest {
  @Test
  public void create() {
    OwnerRepository repo = new OwnerRepository();
    OwnerController controller = new OwnerController(repo);
  }
}
```

### @MockBean
> `@MockBean` 애노테이션으로 가짜 OwnerRepository 객체를 주입 받아서 사용할 수 있음  


## IOC(Inversion Of Container) 컨테이너
> 직접쓸일이 거의 없음  
  
- BeanFactory
- ApplicationContext
  - 자기가 컨테이너 내부에 만든객체들 Bean들의 의존성을 관리해줌

### 의존성 주입
@Autowired 를 이용해 빈을 꺼내와서 의존성을 주입해줄 수 있음  
IOC 컨테이너 자체가 Bean으로 등록되어 있어 아래와 같이 가져 올 수 있음  
```java
@Autowired
ApplicationContext applicationContext
```

## 빈(Bean)
> 스프링 IoC 컨테이너가 관리하는 객체  

## AOP(Abstract Oriented Programming)
> 흩어진 코드를 한 곳으로 모으는 코딩 기법  
> 스프링에서는 Proxy 패턴을 사용하여 AOP를 처리함  

### Annotation
> Annotation 자체는 아무 기능이 없음 주석과 같은 역할임  
> Annotation을 동작하기 위해 Aspect 클래스로 기능을 구현해야함  

#### @Target(ElementType.METHOD)
> 메서드에 붙일거기때문에 METHOD로 지정  

#### @Retention(RetentionPolicy.RUNTIME)
> 애노테이션을 사용한 코드를 언제까지 유지할건지 런타임때까지 유지  

#### @Around("@annotation(LogExecutionTime)")
> 이 애노테이션 주변으로 할일 정의  
- 메서드가 호출되기 전, 후, 오류가 났을 때 다 사용할 수 있는게 Around
- Bean 이라는 어노테이션을 써도됨
- 특정 표현식을 쓰면 @annotation 을 안붙이고도 정의할 수 있음
  
#### annotation: LogExecutionTime
```java
//메서드에 붙일거기때문에 METHOD로 지정
@Target(ElementType.METHOD)
//애노테이션을 사용한 코드를 언제까지 유지할건지 런타임때까지 유지
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {

}
```

#### class: LogAspect
```java
//Component에서 Bean으로 등록
@Component
@Aspect
public class LogAspect {

    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    //이 애노테이션 주변으로 할일 정의
    //메서드가 호출되기 전, 후, 오류가 났을 때 다 사용할 수 있는게 Around
    //특정 표현식을 쓰면 @annotation 을 안붙이고도 정의할 수 있음
    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        //스프링이 제공하는 StopWatch
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Object ret = joinPoint.proceed();

        stopWatch.stop();
        logger.info(stopWatch.prettyPrint());

        return ret;
    }
}
```

## PSA(Portable Service Abstration)
> 잘 만든 인터페이스  
> 스프링은 거의 모든 인터페이스가 PSA임  
> 추상화 되어있는 Abstraction 계층  
> 조금 더 유연하게 코드를 작성할 수있도록 좋은 인터페이스를 만들어서 제공  
> PSA 인터페이스 대부분의 코드는 추상화 되어있으므로 구현체가 바뀌더라도 Aspect 코드가 바뀌지 않음  

### 스프링 트랜잭션(PlatformTransactionManager)
> 기술에 독립적인 `PlatformTransactionManager`인터페이스로 구현을 해놓음  
> `@Transactional` Aspect 안에서 `PlatformTransactionManager`인터페이스를 가져다가 쓰게됨  
> 구현체들이 바뀌더라도 `@Transactional` Aspect의 코드는 바뀌지 않음  
- 구현체
  - JpaTransactionManager
  - DatasourceTransactionManager
  - HibernateTransactionManager

### 스프링 캐시(CacheManager)
> 스프링 캐시의 구현체가 바뀌더라도 `@Cacheable`, `@CacheEvict` Aspect 코드는 바뀌지 않음  
- 구현체
  - JCacheManager
  - ConcurrentMapCacheManager
  - EhCacheCacheManager

### 스프링 웹 MVC(@Controller와 @RequestMapping)
> @Controller과 @RequestMapping 애노테이션만 보고 서블릿을 쓰는지 리액티브를 쓰는지 의존성을 확인해보기 전에 알 수 없음  
> 기술에 독립적으로 만들어진 추상화 구현체  