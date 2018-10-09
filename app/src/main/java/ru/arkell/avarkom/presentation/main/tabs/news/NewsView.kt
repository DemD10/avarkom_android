package ru.arkell.avarkom.presentation.main.tabs.news

import ru.arkell.avarkom.data.response.news.NewsItem
import ru.arkell.avarkom.presentation.base.BaseLCEView

interface NewsView : BaseLCEView {
  fun showNews(news: List<NewsItem>)
}