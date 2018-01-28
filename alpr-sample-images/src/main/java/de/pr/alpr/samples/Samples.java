package de.pr.alpr.samples;

import org.springframework.core.io.ClassPathResource;

public enum Samples {
   CAR001, CAR002, CAR003, CAR004, CAR005, CAR006, CAR007, CAR008, CAR009, CAR010, CAR011, CAR012, CAR013, CAR014, CAR015;

   public ClassPathResource asClassPathResource() {
      return new ClassPathResource(this.name().toLowerCase() + ".jpg", Samples.class);
   }
}
