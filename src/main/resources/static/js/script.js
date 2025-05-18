// src/main/resources/static/js/script.js
document.addEventListener('DOMContentLoaded', function() {
    // Add any JavaScript functionality here
    console.log('Wedding Vendor Booking System loaded');

    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert');
        alerts.forEach(function(alert) {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
});
