package com.antonageev.popularlibs;

import androidx.test.runner.AndroidJUnit4;

import com.antonageev.popularlibs.dagger.DaggerNetModule;
import com.antonageev.popularlibs.models.GitHubUsers;
import com.antonageev.popularlibs.presenters.FourthPresenter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import dagger.Component;
import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.subscribers.TestSubscriber;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


class TestDaggerNetModule extends DaggerNetModule {
    MockWebServer server;

    TestDaggerNetModule(MockWebServer server) {
        this.server = server;
    }

    @Override
    public String provideEndPoint() {
        return server.url("/").toString();
    }
}

@Component(modules = {DaggerNetModule.class})
interface TestNetComponent {
    void inject(ExampleInstrumentedTest exampleInstrumentedTest);
}


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private MockWebServer mockWebServer;

    @Inject
    Single<List<GitHubUsers>> single;

    @Before
    public void prepare() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        DaggerTestNetComponent.builder()
                .daggerNetModule(new TestDaggerNetModule(mockWebServer))
                .build()
                .inject(this);
    }

    @Test
    public void getUsers() {
        mockWebServer.enqueue(getMockCorrectResponse());
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        single.subscribeWith(new DisposableSingleObserver<List<GitHubUsers>>() {
            @Override
            public void onSuccess(List<GitHubUsers> gitHubUsers) {
                if (gitHubUsers.size() == 0) {
                    testSubscriber.onNext(false);
                    testSubscriber.onComplete();
                    return;
                }

                GitHubUsers model = gitHubUsers.get(0);
                boolean equals = model.getLogin().equals("mojombo") &&
                        model.getAvatarUrl().equals("https://avatars0.githubusercontent.com/u/1?v=4") &&
                        model.getUrl().equals("https://api.github.com/users/mojombo");
                testSubscriber.onNext(equals);
                testSubscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                testSubscriber.onError(e);
            }
        });

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(true);
    }

    @Test
    public void getUsersPresenterCheck() {
        FourthPresenter mock4Presenter = Mockito.mock(FourthPresenter.class);
        mockWebServer.enqueue(getMockCorrectResponse());
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        single.subscribeWith(new DisposableSingleObserver<List<GitHubUsers>>() {
            @Override
            public void onSuccess(List<GitHubUsers> gitHubUsers) {
                mock4Presenter.onModelUpdated();
                testSubscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                testSubscriber.onError(e);
            }
        });

        testSubscriber.awaitTerminalEvent();
        Mockito.verify(mock4Presenter).onModelUpdated();
    }

    @Test
    public void getNotFoundUsers() { // в этот ответ включается оба варианта (ошибочный статус-код и некорректный JSON)
        mockWebServer.enqueue(getMockNotFoundResponse());
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        single.subscribeWith(new DisposableSingleObserver<List<GitHubUsers>>() {
            @Override
            public void onSuccess(List<GitHubUsers> gitHubUsers) {
                testSubscriber.onNext(false);
                testSubscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                testSubscriber.onNext(true);
                testSubscriber.onComplete();
            }
        });

        testSubscriber.awaitTerminalEvent();
        testSubscriber.assertValue(true);
    }

    @Test
    public void getNotFoundUsersPresenterCheck() {
        FourthPresenter mock4Presenter = Mockito.mock(FourthPresenter.class);
        mockWebServer.enqueue(getMockNotFoundResponse());
        TestSubscriber<Boolean> testSubscriber = TestSubscriber.create();

        single.subscribeWith(new DisposableSingleObserver<List<GitHubUsers>>() {
            @Override
            public void onSuccess(List<GitHubUsers> gitHubUsers) {
                testSubscriber.onComplete();
            }

            @Override
            public void onError(Throwable e) {
                mock4Presenter.onModelUpdateFailed(Mockito.anyString());
                testSubscriber.onError(e);
            }
        });

        testSubscriber.awaitTerminalEvent();
        Mockito.verify(mock4Presenter).onModelUpdateFailed(Mockito.anyString());
    }


    private MockResponse getMockCorrectResponse() {
        return new MockResponse().setBody("[\n" +
                "    {\n" +
                "        \"login\": \"mojombo\",\n" +
                "        \"id\": 1,\n" +
                "        \"node_id\": \"MDQ6VXNlcjE=\",\n" +
                "        \"avatar_url\": \"https://avatars0.githubusercontent.com/u/1?v=4\",\n" +
                "        \"gravatar_id\": \"\",\n" +
                "        \"url\": \"https://api.github.com/users/mojombo\",\n" +
                "        \"html_url\": \"https://github.com/mojombo\",\n" +
                "        \"followers_url\": \"https://api.github.com/users/mojombo/followers\",\n" +
                "        \"following_url\": \"https://api.github.com/users/mojombo/following{/other_user}\",\n" +
                "        \"gists_url\": \"https://api.github.com/users/mojombo/gists{/gist_id}\",\n" +
                "        \"starred_url\": \"https://api.github.com/users/mojombo/starred{/owner}{/repo}\",\n" +
                "        \"subscriptions_url\": \"https://api.github.com/users/mojombo/subscriptions\",\n" +
                "        \"organizations_url\": \"https://api.github.com/users/mojombo/orgs\",\n" +
                "        \"repos_url\": \"https://api.github.com/users/mojombo/repos\",\n" +
                "        \"events_url\": \"https://api.github.com/users/mojombo/events{/privacy}\",\n" +
                "        \"received_events_url\": \"https://api.github.com/users/mojombo/received_events\",\n" +
                "        \"type\": \"User\",\n" +
                "        \"site_admin\": false\n" +
                "    }]");
    }

    private MockResponse getMockNotFoundResponse() {
        return new MockResponse().setBody("{\n" +
                "    \"message\": \"Not Found\",\n" +
                "    \"documentation_url\": \"https://developer.github.com/v3/users/#get-a-single-user\"\n" +
                "}");
    }

    @After
    public void finish() throws IOException {
        mockWebServer.shutdown();
    }
}
