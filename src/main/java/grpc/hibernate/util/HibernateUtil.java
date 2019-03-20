package grpc.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(HibernateUtil.class);
	private static StandardServiceRegistry registry;
	private static SessionFactory sessionFactory;

	public synchronized static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				LOGGER.info("Creating Hibernate Registry");
				
				registry = new StandardServiceRegistryBuilder().configure().build();

				MetadataSources sources = new MetadataSources(registry);

				Metadata metadata = sources.getMetadataBuilder().build();

				sessionFactory = metadata.getSessionFactoryBuilder().build();

				LOGGER.info("Hibernate Session Factory created");

			} catch (Exception hibernateException) {
				LOGGER.error("Error when starting Hibernate. See details: ", hibernateException);
				if (registry != null) {
					LOGGER.info("Destroying StandardServiceRegistry for Hibernate due exception: "
							+ hibernateException.getMessage());
					StandardServiceRegistryBuilder.destroy(registry);
				}
			}
		}
		return sessionFactory;
	}

	public static void shutdown() {
		if (registry != null) {
			StandardServiceRegistryBuilder.destroy(registry);
			LOGGER.info("Destroyed StandardServiceRegistry for Hibernate. Shut down requested.");
		}
	}
}