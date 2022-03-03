package Work.Work3;

import lombok.*;

import java.io.Serializable;

@Getter
@EqualsAndHashCode
@ToString
public class Cat extends Animal implements Serializable {
    private static final long serialVersionUID = 1L;

    @Setter
    private transient String name;

    @Setter
    private String color;

    public Cat() {
        super("Cat");
        System.out.println("Cat born");
    }

    public Cat(String name, String color) {
        this();
        this.name = name;
        this.color = color;
    }

}