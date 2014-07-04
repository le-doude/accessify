Accessify
=========

###THIS IS NOT IN A WORKING STATE!!!
###DO NOT TRY TO USE!!!! yet.

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

I am still trying to figure how to manage embedded `@HandledType`(s) within other `@HandledType`(s).
I welcome any input on this. And feel free to contribute to the code.

I plan on implementing both Maven and SBT support. I will rely on the community for integrating with any other build tools.

Also my "bill-paying" job is taking a lot of my time so do not expect lightning fast progress on this.

    


