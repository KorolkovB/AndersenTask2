import exceptions.MyIndexOutOfBoundsException;
import myCollection.MyList;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyListTests {
	private static MyList<String> myList;

	@BeforeAll
	static void initAll() {
		myList = new MyList<>();
		myList.add("First");
		myList.add("Second");
		myList.add("Third");
	}

	@Order(1)
	@Test
	void countOfElements() {
		assertEquals(3, myList.size());
	}

	@Order(2)
	@Test
	void canAddNewCollectionWithExistingIndex() {
		MyList<String> newList = new MyList<>();
		newList.add("Fifth");

		myList.addAll(3, newList);

		assertEquals(4, myList.size());
	}

	@Order(3)
	@Test
	void cantAddNewCollectionWithNotExistingIndex() {
		MyList<String> newList = new MyList<>();
		newList.add("Fifth");

		Assertions.assertThrows(MyIndexOutOfBoundsException.class, () -> {
			myList.addAll(5, newList);
		});
	}

	@AfterAll
	static void tearDownAll() {
		myList = null;
	}
}

