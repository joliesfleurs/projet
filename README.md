# Lille 1 Community

Lille 1 Community is university project created in order to help students and employees of the university
to help each other and communicate easily.
It provides tools such as advertisement for car sharing, lending tools, help in math and feedback about experiences abroad.


## Table of contents

* [Technologies used](#technologies-used)
* [Samples of code](#samples-of-code)
	* [Spring MVC architecture](#spring-mvc-architecture)
	* [ Testing with Mockito and spring test framework](#testing-with-mockito-and-spring-test-framework)


## Technologies used

* Spring MVC
* Hibernate
* HTML/CSS
* JQuery
* Foundation library
* Maven
* Mockito


## Samples of code
In this part, I will describe (for you, visitor, and for the future me) which part of this project can illustrate classical
cases of development.
I will put sample of code that could be used and adapted for other projects.

### Spring MVC architecture

```
main/
├── java/
    ├── controller
    ├── dao
    ├── domain
    ├── services
    ├── util

```
As said in the name, controller folder contains all the controllers.
A controller file is annoted by @Controller and @RequestMapping("/SOME_PATH") to indicate which controller will be used
when this url is used. It can also be used for methods as seen below.
```java
@Controller
@RequestMapping("/role")
public class RoleController {
  @Autowired
	private RoleService roleService;
	
	@RequestMapping("new")
	public ModelAndView getRoleForm(@ModelAttribute Role role) {
		return new ModelAndView("role_form");
	}
}
```
@Autowired allows to place an instance of the bean service RoleService into this field. 
The application context directly inject an instance of RoleService

DAO folder contains both interfaces and implementations of the Data Access Object. Hibernates is used to do all the CRUD
operations needed.
Nothing special except get the SessionFactory bean autowired and always use the current session.

```java
@Autowired
private SessionFactory sessionFactory;
```

Domain folder contains all objects useful for the application with simple classes annotated appropriately for hibernate and with 
always an empty constructor and getters/setters.
Example:
```java
@Entity
public class Annonce {
	
	@Id
	@GeneratedValue
	@Column(name="ann_id")
	private int id;
	
	@Column(name="ann_titre")
	private String titre;
	
	@Column(name="ann_desc")
	private String desc;
	
	@Column(name="ann_date_debut")
	private Date date_deb;
	
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Column(name="ann_date_fin")
	private Date date_fin;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="cat_id")
	private Categorie categorie;
	
	//...
	
	public Annonce(){
		
	}
	//...
```

Following the same pattern, there are an interface and the implementation of this one for services
The implemented class has to be annotated by @Service and use again the @Autowired principle to get the DOA needed.
All method using DOA has to be @Transactional. This allows to define the scope of a single database transaction. This way, it will make sure that the changes are well persisted in database. And if something goes wrong, it will just roll back before the beginning of the transaction.
```java
@Service
public class AnnonceServiceImpl implements AnnonceService {

	@Autowired
	AnnonceDao annonceDao;

	@Autowired
	CategorieDao categorieDao;

	@Override
	@Transactional
	public int insertRow(Annonce ann) {
		annonceDao.incrementNbAnnCrees();
		return annonceDao.insertRow(ann);
	}
}
```

Services and DOAs implementation have to be declared in the spring configuration file as bean

```xml
 <bean id="categorieDaoImpl" class="glp.dao.CategorieDaoImpl" />  
 <bean id="categorieServiceImpl" class="glp.services.CategorieServiceImpl" /> 
```

The util folder just contains classes such as Validator, formatter or miscellaneous operations needed and which doesn't fit elsewhere.

### Testing with [Mockito](http://mockito.org/) and spring test framework

The combination of these frameworks provides a lot of possibility to test an application.

Maven dependencies

```xml
<!-- Test -->
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.8.1</version>
	<type>jar</type>
	<scope>compile</scope>
</dependency>
<dependency>
	<groupId>org.mockito</groupId>
	<artifactId>mockito-all</artifactId>
	<version>1.8.5</version>
</dependency>
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-test</artifactId>
	<version>4.1.3.RELEASE</version>
</dependency>
<dependency>
	<groupId>com.jayway.jsonpath</groupId>
	<artifactId>json-path</artifactId>
	<version>0.8.1</version>
	<scope>test</scope>
</dependency>
```
Mockito is a testing framwork especially design for unit testing in Java. It is really easy to use and allow powerful test.
It this project, it is used to test Controller which it is not easy to test with simple JUnit.

```java
public class AnnonceControllerTest extends TestCase{

	/* We initiate controller and services needed, they will be mocks of the real ones in order to not make any changes on 	the databases	*/
	@InjectMocks
	private AnnonceController controller = new AnnonceController();
	
	@Mock
	private AnnonceService mockAnnonceService;
	
	@Mock
	private CategorieService mockCategorieService;
	
	@Mock
	private UtilisateurService mockUtilisateurService;
	
	@Mock
	View mockView;
	
	MockMvc mockMvc;
	
	/*Before actually doing the tests, we set up the mock environment*/
	/*standaloneSetup methid does not load all the spring configuration but create mock out all the dependencies */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setSingleView(mockView).build();
	}

	@Test
	public void testGetCreateAnnForm() throws Exception {
		 List<Categorie> expectedCategorie = Arrays.asList(new Categorie());
		 when(mockCategorieService.getList()).thenReturn(expectedCategorie);
		 
		 Utilisateur user = new Utilisateur();
		 when(mockUtilisateurService.getUserInSession()).thenReturn(user);
		 
		 // Test results with this url, status is ok when there is no error then we compare the result
		this.mockMvc.perform(get("/annonce/new"))
			.andExpect(status().isOk())
			.andExpect(model().attribute("catList", expectedCategorie))
			.andExpect(model().attribute("utilisateur", user));
	}
}
```
