package me.r1411.askapi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "board")
public class Board {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Size(min = 2, max = 32, message = "Title length should be between 2 and 32 characters long")
    @Column(name = "title")
    private String title;

    public Board() {

    }

    public Board(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
