Accessify
=========

This project was never meant to be a working tool that people could use in professional projects. It is just there because I wanted to clear some ideas that I had for making some tools that use Java Reflection faster (for example I had some bad experience with Hibernate and hug lists of entities).

Feel free to use the code here, but find it in your heart to credit me.

Goals
-----

This project is more an experiment on features that I wanted to learn from the Java API (code generation and compile API).
I also have had a lot of cases when I needed to implement my own subset of DAOs to  work with patterns not covered by most ORMs or OCMs (especially when using serialized object or when working with lesser know NoSQL solutions).
A recurring feature was figuring how to access fields and property by their names without using Reflection (notoriously slow). So I figured I'd use a simple pattern:

    interface PropertyHandler<E, P>{
    
        void set(E instance, P propertyValue);
        
        P get(E instance);
    
    }

Now whether this will become a fully supported library or just get abandoned is another matter.

(Projected) Use
----------------

Let's start with a random data class that represent some data structure in your application.

    public class MyClass{
        
        private String string;
        
        private Integer interger;
        
        private Float float;
        
        //With all the getters and setters necessary to make it a standard bean.
        
    }

Mark it as `@HandledType`
    
    @HandledType
    public class MyClass{
        //...
    }
    
And Accessify should generate (on build) all handlers for properties and types. You can access those handlers with:

    MyClass randomInstance = //.....
    ObjectHandler<MyClass> myClassHandler = HandlingFactory.get("").handler(MyClass.class);
    String s = myClassHandler.get(randomInstance, "string");
    Integer i = myClassHandler.get(randomInstance, "integer");
    Float f = myClassHandler.get(randomInstance, "float");
    
    myClassHandler.set(randomInstance, "string", "New Value");
    myClassHandler.set(randomInstance, "integer", 12345);
    myClassHandler.set(randomInstance, "float", .12345f);
    
Simple enough. Whether this is useful to anybody but me is another question entirely.

Developments
------------

embedded `@HandledType`(s) within other `@HandledType`(s). Is done and that was more obvious than anticipated.
Now on to collections, lists, sets and maps. How to work that out?


I plan on implementing both Maven and SBT support. I will rely on the community for integrating with any other build tools.

Also my "bill-paying" job is taking a lot of my time so do not expect lightning fast progress on this.

    


