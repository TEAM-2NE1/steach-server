# LOMBOK ANNOTATION CONVENTION

## Controller
```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/curricula")
public class Controller {}
```
## Service
```java
@Service
@Slf4j
@RequiredArgsConstructor
public class Service {}
```

## Domain
```java
@Entity
@ToString
@Getter(value = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PROTECTED)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teachers")
@PrimaryKeyJoinColumn(name = "id")
public class Domain{
    
    --PK Id--
    
    --핵심 필드--
            
    --기본 필드--
            
    --One To One--

    --Many To One--

    --One To Many--
            
    --One To One (mappedBy 적는거) (DB컬럼에 안들어가는거)--
        
    --createdAt, updatedAt--
    
------
            
    --constructor--
    
    --create(), of()--
    
    --update()--
    
    --delete()--
    
    --기타 메서드--
    
    private는 해당 메서드 바로 밑으로
}
```

## Dto
```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dto {}
```