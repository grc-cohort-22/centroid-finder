const request = require("supertest");
const app = require("./index");

describe("GET /api/videos", () => {

  test("returns a list of videos", async () => {

    const response =
      await request(app).get("/api/videos");

    expect(response.status).toBe(200);

    expect(Array.isArray(response.body))
      .toBe(true);
  });

});