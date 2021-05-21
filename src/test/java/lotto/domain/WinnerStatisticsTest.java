package lotto.domain;

import lotto.utils.LottoNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class WinnerStatisticsTest {

    List<LottoTicket> tickets = new ArrayList<>();

    @BeforeEach
    public void initLottoTicket() {

        for (int i = 0; i < 5; i++) {
            List<LottoNumber> lottoNumbers = makeLottoNumbers(6 * i + 1);
            tickets.add(LottoTicket.create(lottoNumbers));
        }
    }

    private List<LottoNumber> makeLottoNumbers(int startNumber) {
        List<String> lottoNumbers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            String number = String.valueOf(startNumber + i);
            lottoNumbers.add(number);
        }

        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator();

        return lottoNumberGenerator.generator(lottoNumbers);
    }

    @DisplayName("로또당첨등수별 수익률 테스트(5장구입기준)")
    @ParameterizedTest
    @CsvSource(value = {"1,2,3,4,5,6:400000.00", "1,2,3,4,5,40:300.00", "1,2,3,4,35,40:10.00", "1,2,3,30,35,40:1.00", "1,2,25,30,35,40:0.00"}, delimiter = ':')
    public void incomeRateTest(String lottoNumber, String expectedRate) {
        List<LottoNumber> lottoNumbers = makeLottoNumbers(lottoNumber);

        WinnerNumbers winnerNumbers = WinnerNumbers.create(lottoNumbers);

        WinnerStatistics winnerStatistics = WinnerStatistics.create(tickets, winnerNumbers);

        assertThat(winnerStatistics.getIncomeRate()).isEqualTo(expectedRate);
    }

    @DisplayName("로또당첨 손실여부 테스트(5장구입기준)")
    @ParameterizedTest
    @CsvSource(value = {"1,2,3,4,5,6:false", "1,2,3,30,35,40:false", "1,2,25,30,35,40:true"}, delimiter = ':')
    public void isLossTest(String lottoNumber, boolean expectedLoss) {
        List<LottoNumber> lottoNumbers = makeLottoNumbers(lottoNumber);

        WinnerNumbers winnerNumbers = WinnerNumbers.create(lottoNumbers);

        WinnerStatistics winnerStatistics = WinnerStatistics.create(tickets, winnerNumbers);

        assertThat(winnerStatistics.isLoss()).isEqualTo(expectedLoss);
    }

    private List<LottoNumber> makeLottoNumbers(String lottoNumber) {
        String[] splitedLottoNumber = StringUtils.split(lottoNumber);

        List<String> lottoNumberText = Arrays.asList(splitedLottoNumber);
        LottoNumberGenerator lottoNumberGenerator = new LottoNumberGenerator();
        return lottoNumberGenerator.generator(lottoNumberText);
    }
}
