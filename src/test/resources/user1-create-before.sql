INSERT INTO public.users(id, activation_code, active, date_of_create, email, name, password, phone_number, image_id)
VALUES (2, null, true, current_date, 'user1@mail.ru', 'user1', '$2a$08$PM2OCIXYJ3fxlPiXnsbntuW0xEcJXhEiVj8KUh/HzNP/a4T5xhUM2', 123, null);

INSERT INTO public.user_role(user_id, roles)
VALUES (2, 'ROLE_USER');