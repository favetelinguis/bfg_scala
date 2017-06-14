package com.bfg.infrastructure.gui.view

import com.bfg.infrastructure.gui.model.MainStageModel

import scalafx.Includes._
import scalafx.scene.Scene
import scalafx.scene.control._
import scalafx.scene.layout.BorderPane

/**
  * Created by henke on 2017-06-11.
  */
class MainStageView(model: MainStageModel) {

  val scene = new Scene(600, 400) {
      root = new BorderPane() {
        center = createTabs()
        //bottom = createStatusbar()
      }
    }

  private def createTabs() = new TabPane {
    tabs = List(
      new Tab {
        text = "Market Status List"
        content = createMarketStatusList()
        closable = false
      },
      new Tab {
        text = "Market Status"
        content = createMarketStatus()
        closable = false
      },
      new Tab {
        text = "Order Status"
        content = createOrderStatus()
        closable = false
      }
    )
  }

  def createMarketStatusList(): ListView[String] = new ListView[String] {
    items = model.marketSnaps
  }

  private def createMarketStatus() = {
    val treeView = new TreeView[String] {
      minWidth = 150
      showRoot = false
      editable = false
      root = new TreeItem[String] {
        value = "Root"
        children = List(
          new TreeItem("Market 1") {
            children = List(
              new TreeItem("Runner 1")
            )
          }
        )
      }
    }

    val listView = new ListView[String] {
      items = model.marketSnaps
    }

    treeView.selectionModel().selectionMode = SelectionMode.Single
    treeView.selectionModel().selectedItem.onChange(
      (_, _, newTreeItem) => {
        if (newTreeItem != null && newTreeItem.isLeaf) {
          model.marketSnaps.clear()
          for (i <- 1 to 10000) {
            model.marketSnaps += s"${newTreeItem.getValue} $i"
          }
        }
      }
    )

    new SplitPane {
      items ++= List(
        treeView,
        listView
      )
    }
  }

  private def createOrderStatus() = new Accordion {
    panes = List(
      new TitledPane() {
        text = "TitledPane A"
        content = new TextArea {
          text = "TitledPane A content"
        }
      },
      new TitledPane {
        text = "TitledPane B"
        content = new TextArea {
          text = "TitledPane B content"
        }
      },
      new TitledPane {
        text = "TitledPane C"
        content = new TextArea {
          text = "TitledPane C' content"
        }
      }
    )

    expandedPane = panes.head
  }
}
