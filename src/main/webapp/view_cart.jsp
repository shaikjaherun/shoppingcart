<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Shopping Cart</title>
</head>
<body>

    <h2>Products</h2>

    <p>Pen - ₹10.0</p>
    <form action="cart" method="post">
        <input type="hidden" name="id" value="1">
        <button type="submit">Add to Cart</button>
    </form>

    <p>Book - ₹50.0</p>
    <form action="cart" method="post">
        <input type="hidden" name="id" value="2">
        <button type="submit">Add to Cart</button>
    </form>

    <h2>Your Cart</h2>

    <form action="checkout" method="post">
        <button type="submit">Confirm Checkout</button>
    </form>

</body>
</html>