package org.babinkuk.batch.processor;

import org.babinkuk.batch.model.Coffee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * Custom batch processor
 * ItemProcessor interface provides a mechanism to apply some specific business logic during our job execution.
 * To keep things simple, this processor takes an input Coffee object and transforms each of the properties to uppercase.
 */
public class CoffeeItemProcessor implements ItemProcessor<Coffee, Coffee> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CoffeeItemProcessor.class);
	
	@Override
	public Coffee process(final Coffee coffee) {
		String brand = coffee.getBrand().toUpperCase();
		String origin = coffee.getOrigin().toUpperCase();
		String chracteristics = coffee.getCharacteristics().toUpperCase();
		
		Coffee transformedCoffee = new Coffee(brand, origin, chracteristics);
		LOGGER.info("Converting ( {} ) into ( {} )", coffee, transformedCoffee);
		
		return transformedCoffee;
	}
}
