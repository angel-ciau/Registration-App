import { createRouter, createWebHistory } from "vue-router";
import type { RouteLocationNormalized } from "vue-router";
import HomeView from "../views/HomeView.vue";
import LoginView from "../views/LoginView.vue";
import GroupsView from "../views/GroupsView.vue";
import SingleGroupView from "../views/SingleGroupView.vue";
import { useAuthStore } from "../stores/auth";
import { publicApiService } from "../axios";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView
    },
    {
      path: "/login",
      name: "login",
      component: LoginView
    },
    {
      path: "/groups",
      name: "groups",
      component: GroupsView,
      beforeEnter: checkAuth
    },
    {
      path: "/groups/:groupId",
      name: "group",
      component: SingleGroupView,
      beforeEnter: checkAuth
    }
  ]
});

async function checkAuth(_to: RouteLocationNormalized, _from: RouteLocationNormalized) {
  const authStore = useAuthStore();

  if (authStore.isLoggedIn) return true;

  await publicApiService.getNewAccessToken();

  if (authStore.isLoggedIn) return true;

  return false;
}

export default router;
