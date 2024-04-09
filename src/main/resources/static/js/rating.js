function submitRating(value) {
    var hasRated = document.getElementById('selectedRating').textContent.trim() !== '';

    if (!hasRated) {
        var confirmation = confirm('Bạn có chắc đã đánh giá ' + value + ' sao cho bộ phim này?');

        if (confirmation) {
            document.getElementById('selectedRating').textContent = value + ' star(s)';

            // Send the rating to the server using AJAX
            var xhr = new XMLHttpRequest();
            xhr.open("POST", "/ratings", true);
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        // Parse the JSON response
                        var response = JSON.parse(xhr.responseText);

                        // Update the totalReviewCount and averageRating on the page
                        document.getElementById('totalReviewCount').textContent = response.totalReviewCount;

                        // Directly set the averageRating without relying on Thymeleaf
                         var averageRatingSpan = document.querySelector('.average-rating');
                                   averageRatingSpan.textContent = response.averageRating.toFixed(1);


                        // Display an alert message after successful rating
                        alert('Đánh giá thành công!');
                    } else {
                        alert('Có lỗi xảy ra khi đánh giá.');
                    }
                }
            };

            xhr.send("value=" + value);
        }
    } else {
        alert('Bạn đã đánh giá rồi!');
    }
    document.addEventListener('DOMContentLoaded', function () {
        // Lấy giá trị averageRating từ .average-rating
        var averageRating = parseFloat(document.querySelector('.average-rating').textContent);

        // Gọi hàm displayEquivalentStars với giá trị averageRating
        displayEquivalentStars(averageRating);
    });
}