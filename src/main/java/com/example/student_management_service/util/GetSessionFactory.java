package com.example.student_management_service.util;

import com.example.student_management_service.entity.Student;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class GetSessionFactory {
    public org.hibernate.SessionFactory getSessionFactory() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .build();
        try {
            return new MetadataSources(registry)
                    .addAnnotatedClass(Student.class)
                    .buildMetadata()
                    .buildSessionFactory();

        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        return null;

    }
}
