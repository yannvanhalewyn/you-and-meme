(ns you-and-meme.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub ::panel :db/panel)
(re-frame/reg-sub ::current-video :db/current-video)
(re-frame/reg-sub ::request-status :db/request-status)
