/*
 * (c) Copyright 2013 Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of either : 
 * - GNU General Public License as published by
 * the Free Software Foundation version 3 of the License.
 * - EUPL v1.1 : European Union Public Licence
 * 
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License or EUPL for more details.
 *
 * You should have received a copy of the GNU General Public License or 
 * European Union Public Licence along with OpenFlexo. 
 * If not, see <http://www.gnu.org/licenses/>, or http://ec.europa.eu/idabc/eupl.html
 *
 * By the way, this Class is partially inspired from Michael Fiedler 
 * initial API OSLCClient and implementation of LYO4J
 *
 */
package org.openflexo.technologyadapter.oslc.model.io;

import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import org.apache.wink.client.ClientResponse;
import org.eclipse.lyo.oslc4j.client.OslcRestClient;
import org.eclipse.lyo.oslc4j.core.model.AbstractResource;
import org.eclipse.lyo.oslc4j.core.model.CreationFactory;
import org.eclipse.lyo.oslc4j.core.model.OslcMediaType;
import org.eclipse.lyo.oslc4j.core.model.QueryCapability;
import org.eclipse.lyo.oslc4j.core.model.ResourceShape;
import org.eclipse.lyo.oslc4j.core.model.Service;
import org.eclipse.lyo.oslc4j.core.model.ServiceProvider;
import org.eclipse.lyo.oslc4j.core.model.ServiceProviderCatalog;
import org.eclipse.lyo.oslc4j.provider.jena.JenaProvidersRegistry;
import org.eclipse.lyo.oslc4j.provider.json4j.Json4JProvidersRegistry;

public abstract class OSLCClient<T extends AbstractResource> {
	private static final Set<Class<?>> PROVIDERS = new HashSet<Class<?>>();

	private static URI CREATED_RESOURCE_URI;

	private final String baseURI;

	static {
		PROVIDERS.addAll(JenaProvidersRegistry.getProviders());
		PROVIDERS.addAll(Json4JProvidersRegistry.getProviders());
		PROVIDERS.add(ServiceProviderCatalog.class);
		PROVIDERS.add(ServiceProvider.class);
	}

	private final Class<T> resourceType;
	private final Class<T[]> resourceArrayType;

	private final ServiceProviderCatalog spc;
	private final ServiceProvider[] sps;

	@SuppressWarnings("unchecked")
	protected OSLCClient(Class<T> resourceType, String baseURI) {
		super();
		this.resourceType = resourceType;
		this.resourceArrayType = (Class<T[]>) Array.newInstance(resourceType, 0).getClass();
		this.baseURI = baseURI;
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, baseURI + "/catalog", OslcMediaType.APPLICATION_RDF_XML);
		spc = oslcRestClient.getOslcResource(ServiceProviderCatalog.class);
		sps = oslcRestClient.getOslcResources(ServiceProvider[].class);
	}

	protected abstract T getResource();

	protected abstract String getResourceType();

	private static String getCreation(final String mediaType, final String type, ServiceProvider provider) {

		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final CreationFactory[] creationFactories = service.getCreationFactories();

			for (final CreationFactory creationFactory : creationFactories) {
				final URI[] resourceTypes = creationFactory.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						return creationFactory.getCreation().toString();
					}
				}
			}

		}

		return null;
	}

	private static String getQueryBase(final String mediaType, final String type, ServiceProvider provider) {

		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final QueryCapability[] queryCapabilities = service.getQueryCapabilities();

			for (final QueryCapability queryCapability : queryCapabilities) {
				final URI[] resourceTypes = queryCapability.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						return queryCapability.getQueryBase().toString();
					}
				}
			}
		}

		return null;
	}

	private static ResourceShape getResourceShape(final String mediaType, final String type, ServiceProvider provider) {
		final Service[] services = provider.getServices();

		for (final Service service : services) {

			final QueryCapability[] queryCapabilities = service.getQueryCapabilities();

			for (final QueryCapability queryCapability : queryCapabilities) {
				final URI[] resourceTypes = queryCapability.getResourceTypes();

				for (final URI resourceType : resourceTypes) {
					if (resourceType.toString().equals(type)) {
						final URI resourceShape = queryCapability.getResourceShape();

						if (resourceShape != null) {
							final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, resourceShape, mediaType);

							return oslcRestClient.getOslcResource(ResourceShape.class);
						}
					}
				}
			}
		}

		return null;
	}

	protected URI create(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {

		final T resource = getResource();

		final String creation = getCreation(mediaType, getResourceType(), serviceProvider);

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, creation, mediaType);

		final T addedResource = oslcRestClient.addOslcResource(resource);

		return addedResource.getAbout();
	}

	protected void delete(final String mediaType, ServiceProvider serviceProvider) {

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final ClientResponse clientResponse = oslcRestClient.removeOslcResourceReturnClientResponse();

		CREATED_RESOURCE_URI = null;
	}

	protected void retrieve(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {

		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final T resource = oslcRestClient.getOslcResource(resourceType);

	}

	protected void retrieves(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {
		final String queryBase = getQueryBase(mediaType, getResourceType(), serviceProvider);
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, queryBase, mediaType);

		final T[] resources = oslcRestClient.getOslcResources(resourceArrayType);

	}

	protected void update(final String mediaType, ServiceProvider serviceProvider) throws URISyntaxException {
		final OslcRestClient oslcRestClient = new OslcRestClient(PROVIDERS, CREATED_RESOURCE_URI, mediaType);

		final T resource = oslcRestClient.getOslcResource(resourceType);

		final ClientResponse clientResponse = oslcRestClient.updateOslcResourceReturnClientResponse(resource);

		final T updatedResource = oslcRestClient.getOslcResource(resourceType);

	}

	private String getCatalogPath() {
		return baseURI + "/catalog";
	}

	private String getServicePath() {
		return baseURI + "/service";
	}

	public ServiceProvider[] getSps() {
		return sps;
	}

	public ServiceProviderCatalog getSpc() {
		return spc;
	}
}