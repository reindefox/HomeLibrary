package com.reindefox.homelibrary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.reindefox.homelibrary.R;
import com.reindefox.homelibrary.activity.AbstractAuthActivity;
import com.reindefox.homelibrary.activity.ApplicationActivity;
import com.reindefox.homelibrary.auth.AuthorizationUtils;
import com.reindefox.homelibrary.auth.Role;
import com.reindefox.homelibrary.fragment.component.LoadingDialog;
import com.reindefox.homelibrary.server.WebServerSingleton;
import com.reindefox.homelibrary.server.model.Book;
import com.reindefox.homelibrary.server.service.book.BookReadingRequest;
import com.reindefox.homelibrary.server.service.book.BookService;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFragment extends Fragment {

    public static final String bundleName = "book";

    private SharedPreferences sharedPreferences;

    private BookService bookService;

    private View view;

    private Book book;

    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book, container, false);

        loadingDialog = new LoadingDialog(this.getContext(), true);

        TextView descView = view.findViewById(R.id.descriptionText);
        descView.setMovementMethod(new ScrollingMovementMethod());

        sharedPreferences = getContext().getSharedPreferences(AuthorizationUtils.PREFS_USER, Context.MODE_PRIVATE);

        bookService = WebServerSingleton.getInstance()
                .getRetrofit()
                .create(BookService.class);

        // Получаем всю информацию о книге, присваивая значение полю Book
        setBookData();

        // Добавляем кнопки, если есть права администратора
        setupAdminManager();

        // Проверяем связь между юзером и книгой
        verifyBookUserConnection();

        ToggleButton favButton = view.findViewById(R.id.toggleButton);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReadingState(favButton.isChecked());
            }
        });

        Button previewButton = view.findViewById(R.id.previewButton);
        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof ApplicationActivity) {
                    ApplicationActivity activity = (ApplicationActivity) getActivity();
                    activity.getBinding().bottomNavigationView.setSelectedItemId(R.id.reading);

                    ReadingFragment readingFragment = new ReadingFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(bundleName, book);
                    readingFragment.setArguments(bundle);

                    activity.replaceFragment(R.id.appLayout, readingFragment);
                }
            }
        });

        return view;
    }

    private void setupAdminManager() {
        if (sharedPreferences.getString(AbstractAuthActivity.ARG_USER_ROLE, "").equals(String.valueOf(Role.ADMIN))) {
            Button button = view.findViewById(R.id.editBookButton);

            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editBookOpenFragment();
                }
            });
        }
    }

    /**
     * Проверка наличия книги в избранном у пользователя
     */
    private void verifyBookUserConnection() {
        bookService.checkUserReading("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), book.getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        // Статус OK, если у пользователя есть книга
                        setReadingState(response.code() == HttpURLConnection.HTTP_OK);
                        loadingDialog.stopLoading();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                    }
                });
    }

    /**
     * Запуск фрагмента редактирования книги
     */
    private void editBookOpenFragment() {
        BookEditFragment bookEditFragment = new BookEditFragment();

        Bundle bundle = new Bundle();
        // Показываем, что необходимо загружать часть с редактированием книги
        bundle.putSerializable(BookEditFragment.BOOK_DATA, book);
        bundle.putBoolean(BookEditFragment.IS_NEW_BOOK_CREATING, false);
        bookEditFragment.setArguments(bundle);

        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.appLayout, bookEditFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Получение информации о книге из полученных аргументов
     */
    private void setBookData() {
        Bundle bundle = getArguments();

        assert bundle != null;
        book = bundle.getSerializable(bundleName, Book.class);

        if (book == null) {
            Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT)
                    .show();

            return;
        }

        ImageView imageView = view.findViewById(R.id.coverImageBook);
        Glide
                .with(view)
                .load(book.getImageUrl())
                .into(imageView);

        TextView titleView = view.findViewById(R.id.titleBook);
        titleView.setText(book.getTitle());

        TextView authorView = view.findViewById(R.id.authorBook);
        authorView.setText(book.getAuthor());

        TextView descView = view.findViewById(R.id.descriptionText);
        descView.setText(book.getDescription());
    }

    /**
     * Обновление состояния добавления книги в избранное пользователем
     * @param state состояние
     */
    private void updateReadingState(boolean state) {
        BookReadingRequest request = new BookReadingRequest();

        request.setBookId(book.getId());
        request.setState(state);

        bookService.updateReadingState("Bearer " + sharedPreferences.getString(AbstractAuthActivity.ARG_AUTH_TOKEN_TYPE, ""), request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                    }
                });

        setReadingState(state);
    }

    /**
     * Установить состояние для кнопки
     * @param isFavorite состояние
     */
    private void setReadingState(boolean isFavorite) {
        ToggleButton button = view.findViewById(R.id.toggleButton);

        button.setChecked(isFavorite);
    }
}