package ru.assignment.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Repeat;

@Repeat(iterations = 10)
public class CollectionsRandomizedTest extends RandomizedTest {
	private Collection<Integer> underTest;

	@Before
	public void before() {
		int collection = randomInt(4);
		switch (collection) {
		case 0: {
			underTest = new ArrayList<>();
			break;
		}
		case 1: {
			underTest = new LinkedList<>();
			break;
		}
		case 2: {
			underTest = new ArrayDeque<>();
			break;
		}
		case 3: {
			underTest = new Vector<>();
			break;
		}
		case 4: {
			underTest = new Stack<>();
			break;
		}
		default:
			throw new UnsupportedOperationException("Should never happen");
		}

	}

	@Test
	public void testAddElements() {
		List<Integer> addedValues = prepareCollection();
		assertTrue(underTest.containsAll(addedValues));
	}

	private List<Integer> prepareCollection() {
		int count = randomInt(1000);
		List<Integer> addedValues = new ArrayList<>();
		for (int i = 0; i < count; ++i) {
			int value = randomInt();
			underTest.add(value);
			addedValues.add(value);
		}
		return addedValues;
	}
}
