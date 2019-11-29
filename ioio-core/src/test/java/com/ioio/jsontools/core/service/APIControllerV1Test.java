package com.ioio.jsontools.core.service;

public class APIControllerV1Test {
    @Test public void
    lotto_resource_returns_200_with_expected_id_and_winners() {

        when().
                get("/lotto/{id}", 5).
                then().
                statusCode(200).
                body("lotto.lottoId", equalTo(5),
                        "lotto.winners.winnerId", hasItems(23, 54));

    }
}
