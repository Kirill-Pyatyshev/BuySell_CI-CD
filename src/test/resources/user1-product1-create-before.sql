INSERT INTO public.products(id, city, date_of_created, description, preview_image_id, price, title, user_id)
VALUES (1, 'Москва', current_date, 'Product1 user1 test', 1, 1000, 'Product1', 2);

INSERT INTO public.images(id, bytes, content_type, is_preview_image, name, original_file_name, size, product_id)
VALUES (1, null,'image/jpeg', true, 'file1', '624224_0.jpg', 30051, 1);