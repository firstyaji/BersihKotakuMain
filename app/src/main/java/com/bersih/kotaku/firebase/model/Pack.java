package com.bersih.kotaku.firebase.model;

import java.util.Objects;

public class Pack extends Model {
    public String title;
    public String description;
    public String image;
    public Double price;
    public boolean isActive;
    public long createAt;
    public long updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pack)) return false;
        Pack pack = (Pack) o;
        return isActive == pack.isActive && createAt == pack.createAt && updatedAt == pack.updatedAt && id.equals(pack.id) && title.equals(pack.title) && description.equals(pack.description) && image.equals(pack.image) && price.equals(pack.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, image, price, isActive, createAt, updatedAt);
    }
}
