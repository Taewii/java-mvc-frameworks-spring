const $ordersContainer = $('.orders');

$('#cart').on('click', () => {
    fetch('/cart/api/items')
        .then(res => res.json())
        .then(orders => {
            orders.forEach(order => {
                $ordersContainer
                    .append(`<div class="dropdown-item text-center">${order.productName} x ${order.quantity}</div>`);
            });
        });
});
