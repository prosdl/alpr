package de.pr.alpr.samples;

import org.springframework.core.io.ClassPathResource;

public enum Samples {
   CAR001, CAR002, CAR003, CAR004, CAR005;

   public ClassPathResource asClassPathResource() {
      return new ClassPathResource(this.name().toLowerCase() + ".jpg", Samples.class);
   }
}
