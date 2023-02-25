package com.bersih.kotaku.firebase.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Recommend extends Model {
    public String title;
    public String image;
    public boolean isActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Recommend)) return false;
        Recommend recommend = (Recommend) o;
        return isActive == recommend.isActive && id.equals(recommend.id) && title.equals(recommend.title) && image.equals(recommend.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, image, isActive);
    }

    @NonNull
    @Override
    protected Recommend clone() throws CloneNotSupportedException {
        Recommend items = new Recommend();
        items.isActive = this.isActive;
        items.image = this.image;
        items.title = this.title;
        return items;
    }
}
