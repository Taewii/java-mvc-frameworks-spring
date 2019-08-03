const $ordersContainer = $('.orders');

$('#cart').on('click', () => {
    fetch('/cart/api/orders')
        .then(res => res.json())
        .then(orders => {
            if (orders.length === 0) {
                $ordersContainer.append(`<div class="dropdown-item text-center">Cart is empty.</div>`);
            }

            orders.forEach(order => {
                $ordersContainer
                    .append(`<div class="dropdown-item text-center">${order.productName} x ${order.quantity}</div>`);
            });
        });
});
