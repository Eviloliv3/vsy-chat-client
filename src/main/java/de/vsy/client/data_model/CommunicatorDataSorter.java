package de.vsy.client.data_model;

import static java.util.Comparator.naturalOrder;

import de.vsy.shared_transmission.dto.CommunicatorDTO;
import java.util.ArrayList;
import java.util.List;

public class CommunicatorDataSorter {

  private CommunicatorDataSorter() {
  }

  public static List<CommunicatorDTO> sortByFirstLastName(
      final List<CommunicatorDTO> communicatorDTOList) {
    List<CommunicatorDTO> sortedListModel;

    if (communicatorDTOList != null) {
      if (communicatorDTOList.size() > 1) {
        sortedListModel = new ArrayList<>(communicatorDTOList.size());
        sortedListModel.addAll(communicatorDTOList);
        sortedListModel.sort(naturalOrder());
      } else {
        sortedListModel = communicatorDTOList;
      }
    } else {
      sortedListModel = new ArrayList<>();
    }
    return sortedListModel;
  }
}
