package praktikum;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static praktikum.IngredientType.*;

@RunWith(MockitoJUnitRunner.class)
public class BurgerTest {

    private Burger burger;

    @Mock
    private Bun bun;

    @Mock
    private Ingredient sauce;

    @Mock
    private Ingredient filling;

    @Before
    public void setUp() {
        burger = new Burger();
    }
    @Test
    public void setBuns_SetsTheBun() {

        burger.setBuns(bun);
        assertEquals("Булочка должна быть установлена", bun, burger.bun);
    }
    @Test
    public void addIngredient_IncreasesListSize() {

        burger.addIngredient(sauce);
        assertEquals("Размер списка ингредиентов должен увеличиться", 1, burger.ingredients.size());
    }
    @Test
    public void addIngredient_AddsCorrectIngredient() {

        burger.addIngredient(sauce);
        assertEquals("Добавленный ингредиент должен совпадать", sauce, burger.ingredients.get(0));
    }
    @Test
    public void removeIngredient_DecreasesListSize() {

        burger.addIngredient(sauce);
        burger.removeIngredient(0);
        assertTrue("Список ингредиентов должен быть пуст после удаления", burger.ingredients.isEmpty());
    }
    @Test
    public void removeIngredient_RemovesCorrectIngredient() {

        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        burger.removeIngredient(0);
        assertEquals("После удаления должен остаться filling", filling, burger.ingredients.get(0));
        assertEquals("Должен остаться 1 ингредиент", 1, burger.ingredients.size());
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void removeIngredient_WithInvalidIndex_ThrowsException() {

        burger.removeIngredient(0);
    }
    @Test
    public void moveIngredient_ChangesFirstItem() {

        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        burger.moveIngredient(1, 0);
        assertEquals("Первым элементом должен стать filling", filling, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredient_ChangesSecondItem() {

        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        burger.moveIngredient(1, 0);
        assertEquals("Вторым элементом должен стать sauce", sauce, burger.ingredients.get(1));
    }
    @Test(expected = IndexOutOfBoundsException.class)
    public void moveIngredient_WithInvalidIndex_ThrowsException() {

        burger.addIngredient(sauce);
        burger.moveIngredient(0, 5);
    }
    @Test
    public void getPrice_CalculatesPrice() {

        when(bun.getPrice()).thenReturn(100f);
        when(sauce.getPrice()).thenReturn(50f);
        when(filling.getPrice()).thenReturn(30f);

        burger.setBuns(bun);
        burger.addIngredient(sauce);
        burger.addIngredient(filling);
        float price = burger.getPrice();
        assertEquals("Цена должна рассчитываться корректно", 280f, price, 0.01);
    }
    @Test
    public void getPrice_WithoutBun_ReturnsZero() {

        burger.addIngredient(sauce);
        float price = burger.getPrice();
        assertEquals("Цена без булочки должна быть 0", 0f, price, 0.01);
    }
    @Test
    public void getPrice_WithoutIngredients_ReturnsOnlyBunsPrice() {

        when(bun.getPrice()).thenReturn(150f);
        burger.setBuns(bun);
        float price = burger.getPrice();
        assertEquals("Цена только булочек должна быть 300", 300f, price, 0.01);
    }
    @Test
    public void getReceipt_ContainsBunName() {

        when(bun.getName()).thenReturn("Space Bun");
        when(bun.getPrice()).thenReturn(100f);
        burger.setBuns(bun);
        assertTrue("Чек должен содержать имя булочки", burger.getReceipt().contains("Space Bun"));
    }
    @Test
    public void getReceipt_ContainsIngredientType() {
        setupMockForReceipt();
        assertTrue("Чек должен содержать тип ингредиента", burger.getReceipt().contains("sauce"));
    }
    @Test
    public void getReceipt_ContainsIngredientName() {

        setupMockForReceipt();
        assertTrue("Чек должен содержать название ингредиента", burger.getReceipt().contains("Space Sauce"));
    }
    @Test
    public void getReceipt_ContainsPrice() {

        setupMockForReceipt();
        assertTrue("Чек должен содержать итоговую цену", burger.getReceipt().contains("250.0"));
    }
    @Test
    public void getReceipt_FormatsIngredientTypeToLowerCase() {

        when(bun.getName()).thenReturn("bun");
        when(bun.getPrice()).thenReturn(10f);
        when(sauce.getType()).thenReturn(FILLING);
        when(sauce.getName()).thenReturn("cutlet");
        when(sauce.getPrice()).thenReturn(100f);

        burger.setBuns(bun);
        burger.addIngredient(sauce);

        assertTrue("Тип ингредиента должен быть в нижнем регистре", burger.getReceipt().contains("= filling cutlet ="));
    }
    @Test
    public void getReceipt_WithoutIngredients_FormatsCorrectly() {
        // Подготовка
        when(bun.getName()).thenReturn("simple bun");
        when(bun.getPrice()).thenReturn(50f);
        burger.setBuns(bun);

        String receipt = burger.getReceipt();

        assertTrue("Чек должен содержать булочку", receipt.contains("simple bun"));
        assertTrue("Чек должен содержать цену", receipt.contains("Price: 100.0"));
    }
    private void setupMockForReceipt() {
        when(bun.getName()).thenReturn("Space Bun");
        when(bun.getPrice()).thenReturn(100f);
        when(sauce.getType()).thenReturn(SAUCE);
        when(sauce.getName()).thenReturn("Space Sauce");
        when(sauce.getPrice()).thenReturn(50f);

        burger.setBuns(bun);
        burger.addIngredient(sauce);
    }
}