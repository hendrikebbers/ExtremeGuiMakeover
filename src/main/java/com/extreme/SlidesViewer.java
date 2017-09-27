package com.extreme;

import com.extreme.data.SlidesEntry;

public class SlidesViewer {

    public static void showSlides(SlidesEntry entry) {
        System.out.println("title: " + entry.getTitle());
        entry.getSlides().forEach(slide -> System.out.println("   slide: " + slide.getName()));
        System.out.println("-------");
    }
}
