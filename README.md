# Lille 1 Community

Lille 1 Community is university project created in order to help students and employees of the university
to help each other and communicate easily.
It provides tools such as advertisement for car sharing, lending tools, help in math and feedback about experiences abroad.

## Technologies used

This web application is designed with JEE using framework like Spring MVC, Hibernate for the backend and HTML5, CSS, JQuery 
and Foundation library for the frontend.
There is also an example of testing Controller with Mockito.
L1C implements spring MVC system but also the i18n tool. Therefore, this architecture can be used easily for other projects

## In which case this code can be useful to me ?
In this part, I will describe (for you, visitor, and for the future me) which part of this project can illustrate classical
cases of development

* Spring MVC architecture

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
@Autowired allow to place an intance of the bean service RoleService into this field. 
The application context directly inject an instance of RoleService

DAO folder contains both interfaces and implementations of the Data Access Object. Hibernates is used to do all the CRUD
operations needed.
Nothing special except get the SessionFactory bean autowired and always use the current session.

```java
@Autowired
private SessionFactory sessionFactory;
```

Domain folder contains all objects useful for the application with simple classes annoted appropriately for hibernate and with 
always an empty constructor and getters/setters.
Exemple:
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
